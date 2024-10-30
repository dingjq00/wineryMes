package com.mom.winery.quartzjob;

import com.mom.winery.app.WinccDataDealCommons;
import com.mom.winery.entity.*;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.core.security.Authenticated;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@DisallowConcurrentExecution
public class RunliangJob implements Job {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    @Authenticated
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        List<JobConfig> jobConfigList = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mainPhase = :mainPhase")
                .parameter("mainPhase", EnumProcessMainPhase.RUNLIANGDOU)
                .list();
        List<MesRunliangdou> mesRunliangdouList = dataManager.load(MesRunliangdou.class)
                .query("select e from MesRunliangdou e")
                .list();

        List<MesWinccItemConfig> mesWinccItemConfigList = dataManager.load(MesWinccItemConfig.class)
                .query("select e from MesWinccItemConfig e ")
                .list();

        List<MesJiaochi> mesJiaochiList = dataManager.load(MesJiaochi.class)
                .query("select e from MesJiaochi e ")
                .list();


        for (JobConfig jobConfig : jobConfigList) {
            MesArea mesArea = jobConfig.getMesArea();
            Integer areaCode = mesArea.getAreaCode();
            Integer currentWinccId = jobConfig.getWinccId();

            List<MesRunliangdoudouRecord> mesRunliangdoudouRecordList = new ArrayList<>();
            List<MesRunliangdouOperation> mesRunliangdouOperationList = new ArrayList<>();

            /**
             * 获取每个单元的润粮斗
             */
            List<MesRunliangdou> areaRunliangdouList = mesRunliangdouList.stream()
                    .filter(mesRunliangdou -> mesRunliangdou.getMesArea().getAreaCode().equals(areaCode))
                    .sorted(Comparator.comparing(MesRunliangdou::getRunliangdouNo))
                    .toList();
            int areaRunliangdouListSize = areaRunliangdouList.size();
            if(areaRunliangdouListSize == 0) {
                continue;
            }
            /**
             * 解决如果通过 Nifi获取 Wincc 数据出错时的处理
             */
            Integer currentMesWinccId = areaRunliangdouList.stream()
                .map(mesRunliangdou -> Optional.ofNullable(mesRunliangdou.getWinccStartID()).orElse(0))
                .max(Integer::compareTo)
                .orElse(0);
            if(currentMesWinccId > currentWinccId) {
                List<WinccRunliangdou> preWinccRunliangdouList = dataManager.load(WinccRunliangdou.class)
                        .query("select e from WinccRunliangdou e " +
                                "where e.areaNo = :areaCode and e.winccId <= :currentWinccId  " +
                                "order by e.winccId desc")
                        .parameter("areaCode", areaCode)
                        .parameter("currentWinccId", currentWinccId)
                        .maxResults(1)
                        .list();
                if(!preWinccRunliangdouList.isEmpty()) {
                    WinccRunliangdou preWinccRunliangdou = preWinccRunliangdouList.getFirst();
                    // 将这个preWinccRunliangdou的数据更新到mesRunliangdou的数据上
                    resetRunliangdouInMemory(preWinccRunliangdou, areaRunliangdouListSize, areaRunliangdouList, mesWinccItemConfigList, mesJiaochiList, mesArea);
                    List<MesRunliangdoudouRecord> preMesRunliangdoudouRecordList = dataManager.load(MesRunliangdoudouRecord.class)
                            .query("select e from MesRunliangdoudouRecord e " +
                                    "where e.mesRunliangdou.mesArea = :mesArea " +
                                    "and e.winccStartID > :preWinccId " )
                            .parameter("mesArea", mesArea)
                            .parameter("preWinccId", preWinccRunliangdou.getWinccId())
                            .list();
                    dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).removing(preMesRunliangdoudouRecordList));
                }else {
                    clearRunliangdouProperties(areaRunliangdouList, mesArea);
                }
            }


            /**
             * 获取每个单元Wincc 润粮斗待处理的数据
             */
            List<WinccRunliangdou> winccRunliangdouList = dataManager.load(WinccRunliangdou.class)
                    .query("select e from WinccRunliangdou e " +
                            "where e.areaNo = :areaCode and e.winccId > :currentWinccId " +
                            "order by e.winccId")
                    .parameter("areaCode", areaCode)
                    .parameter("currentWinccId", currentWinccId)
                    .list();
            if(winccRunliangdouList.isEmpty()) {
                continue;
            }
            // 获取最大的winccId, 用于更新JobConfig的winccId
            Integer maxWinccId = winccRunliangdouList.stream()
                    .map(WinccRunliangdou::getWinccId)
                    .max(Integer::compareTo)
                    .orElse(0);
            /**
             * 处理Wincc 润粮斗待处理的数据
             */
            for (WinccRunliangdou winccRunliangdou : winccRunliangdouList) {
                Date winccUpdateTime = winccRunliangdou.getStarttime();
                Integer winccId = winccRunliangdou.getWinccId();
                String comment = winccRunliangdou.getComment();
                // 如果comment 为null 或为空，不处理
                if (comment == null || comment.isEmpty()) {
                    continue;
                }
                // Split the comment string by commas
                String[] commentParts = comment.split(",");
                int propertyNumber = commentParts.length/areaRunliangdouListSize;


                for (int i = 0; i < areaRunliangdouListSize; i++) {
                    MesRunliangdou mesRunliangdou = areaRunliangdouList.get(i);
                    int runliangdouNo = mesRunliangdou.getRunliangdouNo();
                    int startIndex = i * propertyNumber;
                    int endIndex = (i + 1) * propertyNumber;
                    String[] runliangdouParts = Arrays.copyOfRange(commentParts, startIndex, endIndex);

                    // winccId
                    Integer rawWinccId = mesRunliangdou.getWinccStartID()==null ? 0 : mesRunliangdou.getWinccStartID();
                    mesRunliangdou.setWinccStartID(winccId);

                    // 执行时间
                    Date rawWinccUpdateTime = mesRunliangdou.getWinccUpdateTime();
                    mesRunliangdou.setWinccUpdateTime(winccUpdateTime);

                    // 润粮斗所在位置
                    MesWinccItemConfig rawLocation = mesRunliangdou.getLocation();
                    int location = Integer.parseInt(runliangdouParts[1]);
                    MesWinccItemConfig locationItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.RLD_LOCATION, location);
                    mesRunliangdou.setLocation(locationItem);

                    // 润粮斗是否为空
                    // 当下的EmptyOrFul
                    MesWinccItemConfig rawEmptyOrFull = mesRunliangdou.getEmptyOrFull();
                    boolean isEmpty = Boolean.parseBoolean(runliangdouParts[2]);
                    int valueNo = isEmpty ? 1 : 0;
                    int finalValueNo = valueNo;
                    MesWinccItemConfig emptyItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.EMPTY_FULL, finalValueNo);
                    mesRunliangdou.setEmptyOrFull(emptyItem);

                    // 丢糟或粮糟
                    // 当下的diuzaoOrliangzao
                    MesWinccItemConfig rawDiuZaoOrliangzao = mesRunliangdou.getDiuZaoOrliangzao();
                    boolean diuzaoOrLiangzao = Boolean.parseBoolean(runliangdouParts[3]);
                    valueNo = diuzaoOrLiangzao ? 1 : 0;
                    int finalValueNo1 = valueNo;
                    MesWinccItemConfig diuzaoOrLiangzaoItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.DIUZAO_LIANGZAO, finalValueNo1);
                    mesRunliangdou.setDiuZaoOrliangzao(diuzaoOrLiangzaoItem);

                    // 润粮加水量
                    // 当下的waterQtyAdd
                    float rawWaterQtyAdd = mesRunliangdou.getWaterQtyAdd() ==null ? 0 : mesRunliangdou.getWaterQtyAdd();
                    float waterQtyAdd = Float.parseFloat(runliangdouParts[4]);
                    mesRunliangdou.setWaterQtyAdd(waterQtyAdd);

                    // 润粮时间
                    float rawRunliangDuration = mesRunliangdou.getRunliangDuration() == null ? 0 : mesRunliangdou.getRunliangDuration();
                    float runliangDuration = Float.parseFloat(runliangdouParts[5]);
                    mesRunliangdou.setRunliangDuration(runliangDuration);

                    // 润粮开始时时间，comment里面是这种2024/10/25 1:22:56，-- 此处为wincc 计算的值
                    Date rawRunliangStartTime = mesRunliangdou.getStartTime();
                    Date runliangStartTime = winccDataDealCommons.convertStringToDate(runliangdouParts[6]);
                    mesRunliangdou.setStartTime(runliangStartTime);

                    // 润粮结束时间-- 此处为wincc 提供的值
                    Date rawRunliangEndTime = mesRunliangdou.getEndTime();
                    Date runliangEndTime = winccDataDealCommons.convertStringToDate(runliangdouParts[7]);
                    mesRunliangdou.setEndTime(runliangEndTime);

                    // 窖池层数
                    Integer rawJiaochiLayer = mesRunliangdou.getJiaochiLayer();
                    int jiaochiLayer = Integer.parseInt(runliangdouParts[8]);
                    mesRunliangdou.setJiaochiLayer(jiaochiLayer);

                    // 出窖窖池编号
                    Integer rawJiaochiNo;
                    MesJiaochi rawJiaochi = mesRunliangdou.getJiaochi();
                    if(mesRunliangdou.getJiaochi() != null){
                        rawJiaochiNo = mesRunliangdou.getJiaochi().getJiaochiNo();
                    }
                    int jiaochiNo = Integer.parseInt(runliangdouParts[9]);
                    MesJiaochi jiaochi = winccDataDealCommons.getMesJiaochi(mesJiaochiList, mesArea, jiaochiNo);
                    mesRunliangdou.setJiaochi(jiaochi);

                    // 出窖时间
                    Date rawJiaochiTime = mesRunliangdou.getJiaochiTime();
                    Date jiaochiTime = winccDataDealCommons.convertStringToDate(runliangdouParts[10]);
                    mesRunliangdou.setJiaochiTime(jiaochiTime);

                    // 糟醅类型
                    EnumZaopeiType rawZaopeiTypeEnum = mesRunliangdou.getZaopeiType();
                    Integer rawZaopeiType =null;
                    if(mesRunliangdou.getZaopeiType() != null){
                        rawZaopeiType = mesRunliangdou.getZaopeiType().getId();
                    }
                    int zaopeiType = Integer.parseInt(runliangdouParts[11]);
                    EnumZaopeiType enumZaopeiType = EnumZaopeiType.fromId(zaopeiType);
                    mesRunliangdou.setZaopeiType(enumZaopeiType);

                    // 稻壳添加量
                    Float rawDaokeAddQty = mesRunliangdou.getDaokeAddQty()==null ? 0 : mesRunliangdou.getDaokeAddQty();
                    float daokeAddQty = Float.parseFloat(runliangdouParts[12]);
                    mesRunliangdou.setDaokeAddQty(daokeAddQty);

                    // 粮食添加量
                    float rawLiangshiAddQty = mesRunliangdou.getLiangshiAddQty()==null ? 0 : mesRunliangdou.getLiangshiAddQty();
                    float liangshiAddQty = Float.parseFloat(runliangdouParts[13]);
                    mesRunliangdou.setLiangshiAddQty(liangshiAddQty);

                    // 糟醅添加量
                    float rawZaopeiAddQty = mesRunliangdou.getZaopeiAddQty()==null ? 0 : mesRunliangdou.getZaopeiAddQty();
                    float zaopeiAddQty = Float.parseFloat(runliangdouParts[14]);
                    mesRunliangdou.setZaopeiAddQty(zaopeiAddQty);

                    // 润粮时间是否合格
                    MesWinccItemConfig rawDurationQualified = mesRunliangdou.getDurationQualified();
                    boolean durationQualified = Boolean.parseBoolean(runliangdouParts[15]);
                    valueNo = durationQualified ? 1 : 0;
                    int finalValueNo2 = valueNo;
                    MesWinccItemConfig durationQualifiedItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.NOTQUALIFIED_QUALIFIED, finalValueNo2);
                    mesRunliangdou.setDurationQualified(durationQualifiedItem);

                    // 粮食类型
                    EnumLiangshiType rawLiangshiType = mesRunliangdou.getLiangshiType();
                    Boolean liangshiType = Boolean.parseBoolean(runliangdouParts[16]);
                    int liangshiTypeValue = liangshiType ? 1 : 0;
                    EnumLiangshiType enumLiangshiType = EnumLiangshiType.fromId(liangshiTypeValue);
                    mesRunliangdou.setLiangshiType(enumLiangshiType);

                    // TODO： 是否Record 的条件可能需要调整
                    if(rawLocation == null || !rawLocation.equals(mesRunliangdou.getLocation())){
                        MesRunliangdoudouRecord record = dataManager.create(MesRunliangdoudouRecord.class);
                        record.setMesRunliangdou(mesRunliangdou);
                        // 将mesRunliangdou的属性复制到 record 的属性上
                        record.setWinccStartID(mesRunliangdou.getWinccStartID());
                        record.setWinccUpdateTime(mesRunliangdou.getWinccUpdateTime());
                        record.setPreLocation(rawLocation);
                        setRecordNormalInfo(record, mesRunliangdou.getLocation(), mesRunliangdou.getEmptyOrFull(), mesRunliangdou.getDiuZaoOrliangzao(), mesRunliangdou.getWaterQtyAdd(), mesRunliangdou.getRunliangDuration(), mesRunliangdou.getStartTime(), mesRunliangdou.getEndTime(), mesRunliangdou.getJiaochiLayer(), mesRunliangdou.getJiaochi(), mesRunliangdou.getJiaochiTime(), mesRunliangdou.getZaopeiType(), mesRunliangdou.getDaokeAddQty(), mesRunliangdou.getLiangshiAddQty(), mesRunliangdou.getZaopeiAddQty(), mesRunliangdou.getDurationQualified(), mesRunliangdou.getLiangshiType());

                        mesRunliangdoudouRecordList.add(record);

                        /**
                         * 检查状态是否需要创建润粮斗操作记录
                         * 33：320-接粮糟
                         * 34：320-接丢糟
                         * // 35：320-接壳
                         */
                        if(record.getLocation() !=null && (record.getLocation().getValueNo() == 33 || record.getLocation().getValueNo() == 34)){
                            MesRunliangdouOperation operation = dataManager.create(MesRunliangdouOperation.class);
                            operation.setMesRunliangdou(record.getMesRunliangdou());
                            operation.setWinccUpdateTime(record.getWinccUpdateTime());
                            operation.setWinccStartID(record.getWinccStartID());
                            operation.setPreLocation(rawLocation);
                            setOperationNormalInfo(operation, record);
                            mesRunliangdouOperationList.add(operation);
                        }
                        /**
                         * 根据状态结束操作信息
                         * 31：提升机 && 满斗
                         * 32：翻转卸料机  && 满斗
                         */
                        if(record.getLocation() != null && record.getLocation().getValueNo() == 32 && record.getEmptyOrFull().getValueNo() == 1) {
                            MesRunliangdouOperation operation = mesRunliangdouOperationList.stream()
                                    .filter(e -> e.getMesRunliangdou().equals(mesRunliangdou)
                                            && e.getWinccUpdateTime().before(record.getWinccUpdateTime())
                                            && (e.getLocation().getValueNo() == 33 || e.getLocation().getValueNo() == 34))
                                    .max(Comparator.comparing(MesRunliangdouOperation::getWinccUpdateTime))
                                    .orElse(null);
                            if(operation != null) {
                                operation.setWinccEndId(record.getWinccStartID());
                                operation.setWinccEndTime(record.getWinccUpdateTime());
                                if(operation.getWinccUpdateTime() != null && operation.getWinccEndTime() != null){
                                    long duration = operation.getWinccEndTime().getTime() - operation.getWinccUpdateTime().getTime();
                                    operation.setPhaseDuration((float) (duration/60000));
                                }
                                operation.setAfterLocation(record.getLocation());
                                setOperationNormalInfo(operation, record);
                                mesRunliangdouOperationList.add(operation);
                            }else {
                                if(rawWinccUpdateTime != null) {
                                    List<MesRunliangdouOperation> preOperations = dataManager.load(MesRunliangdouOperation.class)
                                            .query("select e from MesRunliangdouOperation e " +
                                                    "where e.mesRunliangdou = :mesRunliangdou " +
                                                    "and e.winccUpdateTime <= :winccUpdateTime " +
                                                    "order by e.winccUpdateTime desc")
                                            .parameter("mesRunliangdou", mesRunliangdou)
                                            .parameter("winccUpdateTime", rawWinccUpdateTime)
                                            .maxResults(1)
                                            .list();
                                    if (!preOperations.isEmpty()) {
                                        MesRunliangdouOperation preOperation = preOperations.getFirst();
                                        preOperation.setWinccEndId(record.getWinccStartID());
                                        preOperation.setWinccEndTime(record.getWinccUpdateTime());
                                        if (preOperation.getWinccUpdateTime() != null && preOperation.getWinccEndTime() != null) {
                                            long duration = preOperation.getWinccEndTime().getTime() - preOperation.getWinccUpdateTime().getTime();
                                            preOperation.setPhaseDuration((float) (duration / 60000));
                                        }
                                        preOperation.setAfterLocation(record.getLocation());
                                        setOperationNormalInfo(preOperation, record);
                                        mesRunliangdouOperationList.add(preOperation);
                                    }
                                }
                            }
                        }

                        /**
                         * 处理前面具体步骤的结束信息
                         */
                        MesRunliangdoudouRecord preRecord1 = mesRunliangdoudouRecordList.stream()
                            .filter(e -> e.getMesRunliangdou().equals(mesRunliangdou)
                                    && e.getWinccUpdateTime().before(mesRunliangdou.getWinccUpdateTime()))
                            .max(Comparator.comparing(MesRunliangdoudouRecord::getWinccUpdateTime))
                            .orElse(null);
                        if (preRecord1 != null) {
                            preRecord1.setAfterLocation(mesRunliangdou.getLocation());
                            preRecord1.setWinccEndTime(mesRunliangdou.getWinccUpdateTime());
                            if(preRecord1.getWinccUpdateTime() != null && preRecord1.getWinccEndTime() != null){
                                long duration = preRecord1.getWinccEndTime().getTime() - preRecord1.getWinccUpdateTime().getTime();
                                preRecord1.setPhaseDuration((float) (duration/60000));
                            }
                            preRecord1.setWinccEndId(mesRunliangdou.getWinccStartID());

                            setRecordNormalInfo(preRecord1, rawLocation, rawEmptyOrFull, rawDiuZaoOrliangzao, rawWaterQtyAdd, rawRunliangDuration, rawRunliangStartTime, rawRunliangEndTime, rawJiaochiLayer, rawJiaochi, rawJiaochiTime, rawZaopeiTypeEnum, rawDaokeAddQty, rawLiangshiAddQty, rawZaopeiAddQty, rawDurationQualified, rawLiangshiType);

                            mesRunliangdoudouRecordList.add(preRecord1);
                        }else {
                            if(rawWinccUpdateTime != null) {
                                List<MesRunliangdoudouRecord> preRecords = dataManager.load(MesRunliangdoudouRecord.class)
                                        .query("select e from MesRunliangdoudouRecord e " +
                                                "where e.mesRunliangdou = :mesRunliangdou " +
                                                "and e.winccUpdateTime <= :winccUpdateTime " +
                                                "order by e.winccUpdateTime desc")
                                        .parameter("mesRunliangdou", mesRunliangdou)
                                        .parameter("winccUpdateTime", rawWinccUpdateTime)
                                        .maxResults(1)
                                        .list();
                                if (!preRecords.isEmpty()) {
                                    MesRunliangdoudouRecord preRecord = preRecords.getFirst();
                                    preRecord.setAfterLocation(mesRunliangdou.getLocation());
                                    preRecord.setWinccEndTime(winccUpdateTime);
                                    if (preRecord.getWinccUpdateTime() != null && preRecord.getWinccEndTime() != null) {
                                        long duration = preRecord.getWinccEndTime().getTime() - preRecord.getWinccUpdateTime().getTime();
                                        preRecord.setPhaseDuration((float) (duration / 60000));
                                    }
                                    preRecord.setWinccEndId(mesRunliangdou.getWinccStartID());
                                    setRecordNormalInfo(preRecord, rawLocation, rawEmptyOrFull, rawDiuZaoOrliangzao, rawWaterQtyAdd, rawRunliangDuration, rawRunliangStartTime, rawRunliangEndTime, rawJiaochiLayer, rawJiaochi, rawJiaochiTime, rawZaopeiTypeEnum, rawDaokeAddQty, rawLiangshiAddQty, rawZaopeiAddQty, rawDurationQualified, rawLiangshiType);
                                    mesRunliangdoudouRecordList.add(preRecord);
                                }
                            }
                        }


                    }
                }
            }
            saveData(areaRunliangdouList, mesRunliangdoudouRecordList,mesRunliangdouOperationList, jobConfig, maxWinccId);
            mesRunliangdouOperationList.clear();
            mesRunliangdoudouRecordList.clear();
        }
    }

    private static void setOperationNormalInfo(MesRunliangdouOperation operation, MesRunliangdoudouRecord record) {
        operation.setLocation(record.getLocation());
        operation.setEmptyOrFull(record.getEmptyOrFull());
        operation.setDiuZaoOrliangzao(record.getDiuZaoOrliangzao());
        operation.setWaterQtyAdd(record.getWaterQtyAdd());
        operation.setRunliangDuration(record.getRunliangDuration());
        operation.setStartTime(record.getStartTime());
        operation.setEndTime(record.getEndTime());
        operation.setJiaochiLayer(record.getJiaochiLayer());
        operation.setJiaochi(record.getJiaochi());
        operation.setJiaochiTime(record.getJiaochiTime());
        operation.setZaopeiType(record.getZaopeiType());
        operation.setDaokeAddQty(record.getDaokeAddQty());
        operation.setLiangshiAddQty(record.getLiangshiAddQty());
        operation.setZaopeiAddQty(record.getZaopeiAddQty());
        operation.setDurationQualified(record.getDurationQualified());
        operation.setLiangshiType(record.getLiangshiType());
    }

    private static void setRecordNormalInfo(MesRunliangdoudouRecord preRecord1, MesWinccItemConfig rawLocation, MesWinccItemConfig rawEmptyOrFull, MesWinccItemConfig rawDiuZaoOrliangzao, float rawWaterQtyAdd, float rawRunliangDuration, Date rawRunliangStartTime, Date rawRunliangEndTime, Integer rawJiaochiLayer, MesJiaochi rawJiaochi, Date rawJiaochiTime, EnumZaopeiType rawZaopeiTypeEnum, Float rawDaokeAddQty, float rawLiangshiAddQty, float rawZaopeiAddQty, MesWinccItemConfig rawDurationQualified, EnumLiangshiType rawLiangshiType) {
        preRecord1.setLocation(rawLocation);
        preRecord1.setEmptyOrFull(rawEmptyOrFull);
        preRecord1.setDiuZaoOrliangzao(rawDiuZaoOrliangzao);
        preRecord1.setWaterQtyAdd(rawWaterQtyAdd);
        preRecord1.setRunliangDuration(rawRunliangDuration);
        preRecord1.setStartTime(rawRunliangStartTime);
        preRecord1.setEndTime(rawRunliangEndTime);
        preRecord1.setJiaochiLayer(rawJiaochiLayer);
        preRecord1.setJiaochi(rawJiaochi);
        preRecord1.setJiaochiTime(rawJiaochiTime);
        preRecord1.setZaopeiType(rawZaopeiTypeEnum);
        preRecord1.setDaokeAddQty(rawDaokeAddQty);
        preRecord1.setLiangshiAddQty(rawLiangshiAddQty);
        preRecord1.setZaopeiAddQty(rawZaopeiAddQty);
        preRecord1.setDurationQualified(rawDurationQualified);
        preRecord1.setLiangshiType(rawLiangshiType);
    }

    private void resetRunliangdouInMemory(WinccRunliangdou preWinccRunliangdou, int areaRunliangdouListSize, List<MesRunliangdou> areaRunliangdouList, List<MesWinccItemConfig> mesWinccItemConfigList, List<MesJiaochi> mesJiaochiList, MesArea mesArea) {
        String comment = preWinccRunliangdou.getComment();
        String[] commentParts = comment.split(",");
        int propertyNumber = commentParts.length/ areaRunliangdouListSize;
        for (int i = 0; i < areaRunliangdouListSize; i++) {
            MesRunliangdou mesRunliangdou1 = areaRunliangdouList.get(i);
            int startIndex = i * propertyNumber;
            int endIndex = (i + 1) * propertyNumber;
            String[] runliangdouParts = Arrays.copyOfRange(commentParts, startIndex, endIndex);
            // winccId
            mesRunliangdou1.setWinccStartID(preWinccRunliangdou.getWinccId());
            // 执行时间
            mesRunliangdou1.setWinccUpdateTime(preWinccRunliangdou.getStarttime());
            // 润粮斗所在位置
            int location = Integer.parseInt(runliangdouParts[1]);
            MesWinccItemConfig locationItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.RLD_LOCATION, location);
            mesRunliangdou1.setLocation(locationItem);
            // 润粮斗是否为空
            boolean isEmpty = Boolean.parseBoolean(runliangdouParts[2]);
            int valueNo = isEmpty ? 1 : 0;
            int finalValueNo = valueNo;
            MesWinccItemConfig emptyItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.EMPTY_FULL, finalValueNo);
            mesRunliangdou1.setEmptyOrFull(emptyItem);
            // 丢糟或粮糟
            boolean diuzaoOrLiangzao = Boolean.parseBoolean(runliangdouParts[3]);
            valueNo = diuzaoOrLiangzao ? 1 : 0;
            int finalValueNo1 = valueNo;
            MesWinccItemConfig diuzaoOrLiangzaoItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.DIUZAO_LIANGZAO, finalValueNo1);
            mesRunliangdou1.setDiuZaoOrliangzao(diuzaoOrLiangzaoItem);
            // 润粮加水量
            float waterQtyAdd = Float.parseFloat(runliangdouParts[4]);
            mesRunliangdou1.setWaterQtyAdd(waterQtyAdd);
            // 润粮时间
            float runliangDuration = Float.parseFloat(runliangdouParts[5]);
            mesRunliangdou1.setRunliangDuration(runliangDuration);
            // 润粮开始时时间，comment里面是这种2024/10/25 1:22:56
            Date runliangStartTime = winccDataDealCommons.convertStringToDate(runliangdouParts[6]);
            mesRunliangdou1.setStartTime(runliangStartTime);
            // 润粮结束时间
            Date runliangEndTime = winccDataDealCommons.convertStringToDate(runliangdouParts[7]);
            mesRunliangdou1.setEndTime(runliangEndTime);
            // 窖池层数
            int jiaochiLayer = Integer.parseInt(runliangdouParts[8]);
            mesRunliangdou1.setJiaochiLayer(jiaochiLayer);
            // 出窖窖池编号
            int jiaochiNo = Integer.parseInt(runliangdouParts[9]);
            MesJiaochi jiaochi = winccDataDealCommons.getMesJiaochi(mesJiaochiList, mesArea, jiaochiNo);
            mesRunliangdou1.setJiaochi(jiaochi);
            // 出窖时间
            Date jiaochiTime = winccDataDealCommons.convertStringToDate(runliangdouParts[10]);
            mesRunliangdou1.setJiaochiTime(jiaochiTime);
            // 糟醅类型
            int zaopeiType = Integer.parseInt(runliangdouParts[11]);
            EnumZaopeiType enumZaopeiType = EnumZaopeiType.fromId(zaopeiType);
            mesRunliangdou1.setZaopeiType(enumZaopeiType);
            // 稻壳添加量
            float daokeAddQty = Float.parseFloat(runliangdouParts[12]);
            mesRunliangdou1.setDaokeAddQty(daokeAddQty);
            // 粮食添加量
            float liangshiAddQty = Float.parseFloat(runliangdouParts[13]);
            mesRunliangdou1.setLiangshiAddQty(liangshiAddQty);
            // 糟醅添加量
            float zaopeiAddQty = Float.parseFloat(runliangdouParts[14]);
            mesRunliangdou1.setZaopeiAddQty(zaopeiAddQty);
            // 润粮时间是否合格
            boolean durationQualified = Boolean.parseBoolean(runliangdouParts[15]);
            valueNo = durationQualified ? 1 : 0;
            int finalValueNo2 = valueNo;
            MesWinccItemConfig durationQualifiedItem = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList, EnumMesWinccItemConfig.NOTQUALIFIED_QUALIFIED, finalValueNo2);
            mesRunliangdou1.setDurationQualified(durationQualifiedItem);
            // 粮食类型
            Boolean liangshiType = Boolean.parseBoolean(runliangdouParts[16]);
            int liangshiTypeValue = liangshiType ? 1 : 0;
            EnumLiangshiType enumLiangshiType = EnumLiangshiType.fromId(liangshiTypeValue);
            mesRunliangdou1.setLiangshiType(enumLiangshiType);
        }
    }

    @Transactional
    public void saveData(List<MesRunliangdou> areaRunliangdouList, List<MesRunliangdoudouRecord> mesRunliangdoudouRecordList,List<MesRunliangdouOperation> mesRunliangdouOperationList, JobConfig jobConfig, Integer maxWinccId) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(areaRunliangdouList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesRunliangdoudouRecordList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesRunliangdouOperationList));
        jobConfig.setWinccId(maxWinccId);
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(jobConfig));
    }

    private void clearRunliangdouProperties(List<MesRunliangdou> areaRunliangdouList, MesArea mesArea) {
        List<MesRunliangdoudouRecord> preMesRunliangdoudouRecordList = dataManager.load(MesRunliangdoudouRecord.class)
                .query("select e from MesRunliangdoudouRecord e where e.mesRunliangdou.mesArea = :mesArea ")
                .parameter("mesArea", mesArea)
                .list();
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).removing(preMesRunliangdoudouRecordList));
        for (MesRunliangdou mesRunliangdou : areaRunliangdouList) {
            resetRunliangdouProperties(mesRunliangdou);
        }
    }

    private void resetRunliangdouProperties(MesRunliangdou mesRunliangdou) {
        mesRunliangdou.setWinccStartID(null);
        mesRunliangdou.setWinccUpdateTime(null);
        mesRunliangdou.setLocation(null);
        mesRunliangdou.setEmptyOrFull(null);
        mesRunliangdou.setDiuZaoOrliangzao(null);
        mesRunliangdou.setWaterQtyAdd(0f);
        mesRunliangdou.setRunliangDuration(0f);
        mesRunliangdou.setStartTime(null);
        mesRunliangdou.setEndTime(null);
        mesRunliangdou.setJiaochiLayer(0);
        mesRunliangdou.setJiaochi(null);
        mesRunliangdou.setJiaochiTime(null);
        mesRunliangdou.setZaopeiType(null);
        mesRunliangdou.setDaokeAddQty(0f);
        mesRunliangdou.setLiangshiAddQty(0f);
        mesRunliangdou.setZaopeiAddQty(0f);
        mesRunliangdou.setDurationQualified(null);
        mesRunliangdou.setLiangshiType(null);
    }
}
