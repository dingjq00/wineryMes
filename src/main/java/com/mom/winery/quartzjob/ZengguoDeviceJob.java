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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 14:09
 */
@DisallowConcurrentExecution
public class ZengguoDeviceJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(ZengguoDeviceJob.class);
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;
    @Autowired
    private JobSaveDataService jobSaveDataService;

    @Authenticated
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<JobConfig> jobConfigList = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mainPhase = :mainPhase")
                .parameter("mainPhase", EnumProcessMainPhase.ZENGDEVICE)
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
        List<MesZengguo> mesZengguoList = dataManager.load(MesZengguo.class)
                .query("select e from MesZengguo e")
                .list();
        List<MesZenggouPhaseConfig> mesZenggouPhaseConfigList = dataManager.load(MesZenggouPhaseConfig.class)
                .query("select e from MesZenggouPhaseConfig e")
                .list();

        for (JobConfig jobConfig : jobConfigList) {
            List<MesTanliangjiRecord> mesTanliangjiRecordList = new ArrayList<>();

            MesArea mesArea = jobConfig.getMesArea();
            Integer areaCode = mesArea.getAreaCode();
            Integer currentWinccId = jobConfig.getWinccId();
            /**
             * 获取每个单元的甑锅
             */
            List<MesZengguo> areaZengguoList = mesZengguoList.stream()
                    .filter(mesZengguo -> mesZengguo.getMesArea().equals(mesArea))
                    .sorted(Comparator.comparing(MesZengguo::getZengguoCode))
                    .toList();
            int areaZengguoListSize = areaZengguoList.size();
            if (areaZengguoListSize == 0) {
                continue;
            }

            /**
             * 获取每个单元Wincc 甑锅设备待处理的数据
             */
            List<WinccZengdevice> winccZengdeviceList = dataManager.load(WinccZengdevice.class)
                    .query("select e from WinccZengdevice e " +
                            "where e.areaNo = :areaCode and e.winccId > :currentWinccId " +
                            "order by e.winccId")
                    .parameter("areaCode", areaCode)
                    .parameter("currentWinccId", currentWinccId)
                    .maxResults(1000)
                    .list();
            if (winccZengdeviceList.isEmpty()) {
                continue;
            }
            // 获取最大的winccId, 用于更新JobConfig的winccId
            Integer maxWinccId = winccZengdeviceList.stream()
                    .map(WinccZengdevice::getWinccId)
                    .max(Integer::compareTo)
                    .orElse(0);
            List<MesZengguoRecord> mesZengguoRecordList = new ArrayList<>();
            List<MesZengguoOperation> mesZengguoOperationList = new ArrayList<>();
            List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList = new ArrayList<>();
            /**
             * 处理Wincc 甑锅设备待处理的数据
             */

            for (WinccZengdevice winccZengdevice : winccZengdeviceList) {
                Date winccUpdateTime = winccZengdevice.getStarttime();
                Integer winccId = winccZengdevice.getWinccId();
                String comment = winccZengdevice.getComment();
                // 如果comment 为null 或为空，不处理
                if (comment == null || comment.isEmpty()) {
                    continue;
                }
                // Split the comment string by commas
                String[] commentParts = comment.split(",");
                int propertyNumber = commentParts.length / areaZengguoListSize;
                for (int i = 0; i < areaZengguoListSize; i++) {
                    MesZengguo mesZengguo = areaZengguoList.get(i);
                    Date rawUpdateTime = mesZengguo.getWinccUpdateTime();
                    Integer rawUpdateId = mesZengguo.getWinccUpdatId()==null?0:mesZengguo.getWinccUpdatId();
                    mesZengguo.setWinccUpdateTime(winccUpdateTime);
                    mesZengguo.setWinccUpdatId(winccId);

                    int startIndex = i * propertyNumber;
                    int endIndex = (i + 1) * propertyNumber;
                    String[] tanliangjiParts = Arrays.copyOfRange(commentParts, startIndex, endIndex);

                    // 甑锅任务开始时间	0
                    Date rawStartTimeTotal = mesZengguo.getStartTimeTotal();
                    Date startTimeTotal = winccDataDealCommons.convertStringToDate(tanliangjiParts[0]);
                    mesZengguo.setStartTimeTotal(startTimeTotal);

                    //机器人上甑开始时间	1
                    Date rawShangzengStartTime = mesZengguo.getStartTimeDeviceShangZeng();
                    Date shangzengStartTime = winccDataDealCommons.convertStringToDate(tanliangjiParts[1]);
                    mesZengguo.setStartTimeDeviceShangZeng(shangzengStartTime);
                    //卡盘馏酒开始时间	2
                    Date rawKagaiTime = mesZengguo.getStartTimeKagai();
                    Date kagaiTime = winccDataDealCommons.convertStringToDate(tanliangjiParts[2]);
                    mesZengguo.setStartTimeKagai(kagaiTime);
                    //馏酒结束时间	3
                    Date rawLiujiuEndTime = mesZengguo.getEndTimeLiujiu();
                    Date liujiuEndTime = winccDataDealCommons.convertStringToDate(tanliangjiParts[3]);
                    mesZengguo.setEndTimeLiujiu(liujiuEndTime);
                    //甑任务结束时间	4
                    Date rawEndTimeTotal = mesZengguo.getEndTimeTall();
                    Date endTimeTotal = winccDataDealCommons.convertStringToDate(tanliangjiParts[4]);
                    mesZengguo.setEndTimeTall(endTimeTotal);
                    //粗粮（下半甑）：糟源头—窖池号	5
                    MesJiaochi rawJiaochiDown = mesZengguo.getJiaochiDown();
                    Integer jiaochiDownNo = Integer.parseInt(tanliangjiParts[5]);
                    MesJiaochi jiaochiDown = mesJiaochiList.stream()
                            .filter(e -> e.getJiaochiNo().equals(jiaochiDownNo))
                            .findFirst().orElse(null);
                    mesZengguo.setJiaochiDown(jiaochiDown);
                    //粗粮（下半甑）：糟源头—出窖时间	6
                    Date rawJiaochiDownTime = mesZengguo.getJiaochiTimeDown();
                    Date jiaochiDownTime = winccDataDealCommons.convertStringToDate(tanliangjiParts[6]);
                    mesZengguo.setJiaochiTimeDown(jiaochiDownTime);
                    //粗粮（下半甑）：糟源头—出窖层数	7
                    Integer rawJiaochiDownLayer = mesZengguo.getJiaochiLayerDown();
                    mesZengguo.setJiaochiLayerDown(Integer.parseInt(tanliangjiParts[7]));
                    //粗粮（下半甑）：糟源头—糟醅类型	8
                    EnumZaopeiType rawZaopeiTypeDown = mesZengguo.getZaopeiTypeDown();
                    EnumZaopeiType zaopeiTypeDown = EnumZaopeiType.fromId(Integer.parseInt(tanliangjiParts[8]));
                    mesZengguo.setZaopeiTypeDown(zaopeiTypeDown);
                    //粗粮（下半甑）：润粮加水量	9
                    float rawRunliangWaterAddDown = mesZengguo.getRunliangAddWaterDown() ==null?0:mesZengguo.getRunliangAddWaterDown();
                    mesZengguo.setRunliangAddWaterDown(Float.parseFloat(tanliangjiParts[9]));
                    //粗粮（下半甑）：润粮时长	10
                    float rawRunliangDurationDown = mesZengguo.getRunliangDurationDown() ==null?0:mesZengguo.getRunliangDurationDown();
                    mesZengguo.setRunliangDurationDown(Float.parseFloat(tanliangjiParts[10]));
                    //粗粮（下半甑）：糟醅量	11
                    float rawZaopeiQtyDown = mesZengguo.getZaopeiQtyDown()==null?0:mesZengguo.getZaopeiQtyDown();
                    mesZengguo.setZaopeiQtyDown(Float.parseFloat(tanliangjiParts[11]));
                    //粗粮（下半甑）：加稻壳量	12
                    float rawDaokeQtyDown = mesZengguo.getDaokeQtyDown()==null?0:mesZengguo.getDaokeQtyDown();
                    mesZengguo.setDaokeQtyDown(Float.parseFloat(tanliangjiParts[12]));
                    //粗粮（下半甑）：加粮量	13
                    float rawLiangshiQtyDown = mesZengguo.getLiangshiQtyDown()==null?0:mesZengguo.getLiangshiQtyDown();
                    mesZengguo.setLiangshiQtyDown(Float.parseFloat(tanliangjiParts[13]));
                    //粗粮（下半甑）：加粮类型	14
                    EnumLiangshiType rawLiangshiTypeDown = mesZengguo.getLiangshiTypeDown();
                    EnumLiangshiType liangshiTypeDown = EnumLiangshiType.fromId(Boolean.parseBoolean(tanliangjiParts[14])?1:0);
                    mesZengguo.setLiangshiTypeDown(liangshiTypeDown);
                    //细粮：糟源头—窖池号	15
                    MesJiaochi rawJiaochiUp = mesZengguo.getJiaochiUp();
                    Integer jiaochiUpNo = Integer.parseInt(tanliangjiParts[15]);
                    MesJiaochi jiaochiUp = mesJiaochiList.stream()
                            .filter(e -> e.getJiaochiNo().equals(jiaochiUpNo))
                            .findFirst().orElse(null);
                    mesZengguo.setJiaochiUp(jiaochiUp);
                    //细粮：糟源头—出窖时间	16
                    Date rawJiaochiUpTime = mesZengguo.getJiaochiTimeUp();
                    Date jiaochiUpTime = winccDataDealCommons.convertStringToDate(tanliangjiParts[16]);
                    mesZengguo.setJiaochiTimeUp(jiaochiUpTime);
                    //细粮：糟源头—出窖层数	17
                    Integer rawJiaochiUpLayer = mesZengguo.getJiaochiLayerUp()==null?0:mesZengguo.getJiaochiLayerUp();
                    mesZengguo.setJiaochiLayerUp(Integer.parseInt(tanliangjiParts[17]));
                    //细粮：糟源头—糟醅类型	18
                    EnumZaopeiType rawZaopeiTypeUp = mesZengguo.getZaopeiTypeUp();
                    EnumZaopeiType zaopeiTypeUp = EnumZaopeiType.fromId(Integer.parseInt(tanliangjiParts[18]));
                    mesZengguo.setZaopeiTypeUp(zaopeiTypeUp);
                    //细粮：润粮加水量	19
                    float rawRunliangWaterAddUp = mesZengguo.getRunliangAddWaterUp()==null?0:mesZengguo.getRunliangAddWaterUp();
                    mesZengguo.setRunliangAddWaterUp(Float.parseFloat(tanliangjiParts[19]));
                    //细粮：润粮时长	20
                    float rawRunliangDurationUp = mesZengguo.getRunliangDurationUp()==null?0:mesZengguo.getRunliangDurationUp();
                    mesZengguo.setRunliangDurationUp(Float.parseFloat(tanliangjiParts[20]));
                    //细粮：糟醅量	21
                    float rawZaopeiQtyUp = mesZengguo.getZaopeiQtyUp()==null?0:mesZengguo.getZaopeiQtyUp();
                    mesZengguo.setZaopeiQtyUp(Float.parseFloat(tanliangjiParts[21]));
                    //细粮：加稻壳量	22
                    float rawDaokeQtyUp = mesZengguo.getDaokeQtyUp()==null?0:mesZengguo.getDaokeQtyUp();
                    mesZengguo.setDaokeQtyUp(Float.parseFloat(tanliangjiParts[22]));
                    //细粮：加粮量	23
                    float rawLiangshiQtyUp = mesZengguo.getLiangshiQtyUp()==null?0:mesZengguo.getLiangshiQtyUp();
                    mesZengguo.setLiangshiQtyUp(Float.parseFloat(tanliangjiParts[23]));
                    //细粮：加粮类型	24
                    EnumLiangshiType rawLiangshiTypeUp = mesZengguo.getLiangshiTypUp();
                    EnumLiangshiType liangshiTypeUp = EnumLiangshiType.fromId(Boolean.parseBoolean(tanliangjiParts[24])?1:0);
                    mesZengguo.setLiangshiTypUp(liangshiTypeUp);

                    //甑锅工作阶段	25
                    MesZenggouPhaseConfig rawPhase = mesZengguo.getZengguoPhase();
                    Integer phaseNo = Integer.parseInt(tanliangjiParts[25]);
                    MesZenggouPhaseConfig phase = mesZenggouPhaseConfigList.stream()
                            .filter(e -> e.getPhaseNo().equals(phaseNo))
                            .findFirst().orElse(null);
                    mesZengguo.setZengguoPhase(phase);
                    //糟醅类型	26
                    EnumZaopeiType rawZaopeiType = mesZengguo.getZaopeiType();
                    EnumZaopeiType zaopeiType = EnumZaopeiType.fromId(Integer.parseInt(tanliangjiParts[26]));
                    mesZengguo.setZaopeiType(zaopeiType);
                    //上甑层数	27
                    Integer rawShangzengLayer = mesZengguo.getShangzengLayer()==null?0:mesZengguo.getShangzengLayer();
                    mesZengguo.setShangzengLayer(Integer.parseInt(tanliangjiParts[27]));
                    //上甑时长	28
                    float rawShangzengDuration = mesZengguo.getShangzengDuration()==null?0:mesZengguo.getShangzengDuration();
                    mesZengguo.setShangzengDuration(Float.parseFloat(tanliangjiParts[28]));
                    //上甑高度	29
                    float rawShangzengHeight = mesZengguo.getShangzengHeight()==null?0:mesZengguo.getShangzengHeight();
                    mesZengguo.setShangzengHeight(Float.parseFloat(tanliangjiParts[29]));
                    //接酒时长_1级	30
                    float rawJiejiuFirstClassDuration = mesZengguo.getJiejiuDurationFirstClass()==null?0:mesZengguo.getJiejiuDurationFirstClass();
                    mesZengguo.setJiejiuDurationFirstClass(Float.parseFloat(tanliangjiParts[30]));
                    //接酒时长_2级	31
                    float rawJiejiuSecondClassDuration = mesZengguo.getJiejiuDurationSecondClass()==null?0:mesZengguo.getJiejiuDurationSecondClass();
                    mesZengguo.setJiejiuDurationSecondClass(Float.parseFloat(tanliangjiParts[31]));
                    //接酒时长_3级	32
                    float rawJiejiuThirdClassDuration = mesZengguo.getJiejiuDurationThirdClass()==null?0:mesZengguo.getJiejiuDurationThirdClass();
                    mesZengguo.setJiejiuDurationThirdClass(Float.parseFloat(tanliangjiParts[32]));
                    //接酒时长_废水	33
                    float rawJiejiuDurationFeishui = mesZengguo.getJiejiuDurationFeishui()==null?0:mesZengguo.getJiejiuDurationFeishui();
                    mesZengguo.setJiejiuDurationFeishui(Float.parseFloat(tanliangjiParts[33]));
                    //接酒时长_酒尾	34
                    float rawJiejiuDurationJiuwei = mesZengguo.getJiejiuDurationJiuwei()==null?0:mesZengguo.getJiejiuDurationJiuwei();
                    mesZengguo.setJiejiuDurationJiuwei(Float.parseFloat(tanliangjiParts[34]));
                    //晾水添加量	35
                    float rawLiangshuiAddQty = mesZengguo.getLiangshuiAddQty()==null?0:mesZengguo.getLiangshuiAddQty();
                    mesZengguo.setLiangshuiAddQty(Float.parseFloat(tanliangjiParts[35]));
                    //添加量_回收底锅	36
                    float rawHuishoudiguoWaterAddQty = mesZengguo.getHuishoudiguoWaterAddQty()==null?0:mesZengguo.getHuishoudiguoWaterAddQty();
                    mesZengguo.setHuishoudiguoWaterAddQty(Float.parseFloat(tanliangjiParts[36]));
                    //添加量_热水	37
                    float rawHotWaterAddQty = mesZengguo.getHotWaterAddQty()==null?0:mesZengguo.getHotWaterAddQty();
                    mesZengguo.setHotWaterAddQty(Float.parseFloat(tanliangjiParts[37]));
                    //添加量_酒尾	38
                    float rawJiuweiAddQty = mesZengguo.getJiuweiAddQty()==null?0:mesZengguo.getJiuweiAddQty();
                    mesZengguo.setJiuweiAddQty(Float.parseFloat(tanliangjiParts[38]));
                    //添加量_黄水	39
                    float rawHuangshuiAddQty = mesZengguo.getHuangshuiAddQty()==null?0:mesZengguo.getHuangshuiAddQty();
                    mesZengguo.setHuangshuiAddQty(Float.parseFloat(tanliangjiParts[39]));
                    //甑序	40
                    Integer rawZengSequence = mesZengguo.getZengSequence()==null?0:mesZengguo.getZengSequence();
                    mesZengguo.setZengSequence(Integer.parseInt(tanliangjiParts[40]));
                    //耗汽量_上甑	41
                    float rawEnergyQiShangzeng = mesZengguo.getEnergyQiShangzeng()==null?0:mesZengguo.getEnergyQiShangzeng();
                    mesZengguo.setEnergyQiShangzeng(Float.parseFloat(tanliangjiParts[41]));
                    //耗汽量_蒸馏	42
                    float rawEnergyQiZhengliu = mesZengguo.getEnergyQiZhengliu()==null?0:mesZengguo.getEnergyQiZhengliu();
                    mesZengguo.setEnergyQiZhengliu(Float.parseFloat(tanliangjiParts[42]));
                    //馏酒+蒸煮时长	43
                    float rawLiujiuAddZhengzhuDuration = mesZengguo.getLiujiuAddZhengzhuDuration()==null?0:mesZengguo.getLiujiuAddZhengzhuDuration();
                    mesZengguo.setLiujiuAddZhengzhuDuration(Float.parseFloat(tanliangjiParts[43]));


                    // TODO： 是否Record 的条件可能需要调整
                    if (rawPhase == null || !rawPhase.equals(mesZengguo.getZengguoPhase())) {
                        MesZengguoRecord record = dataManager.create(MesZengguoRecord.class);
                        record.setMesZengguo(mesZengguo);
                        // 将mesZengguo的属性复制到 record 的属性上
                        record.setPhaseStartTimeTotal(winccUpdateTime);
                        record.setPhaseStartWinccId(winccId);
                        setNewRecordNormalInfo(record, mesZengguo);
                        mesZengguoRecordList.add(record);


                        // 设置 Record结束
                        MesZengguoRecord preRecord = mesZengguoRecordList.stream()
                                .filter(e -> e.getMesZengguo().equals(mesZengguo)
                                        && e.getPhaseStartTimeTotal().before(mesZengguo.getWinccUpdateTime())
                                        && e.getPhaseEndTimeTotal() == null)
                                .max(Comparator.comparing(MesZengguoRecord::getPhaseStartTimeTotal))
                                .orElse(null);
                        if (preRecord != null) {
                            preRecord.setPhaseEndTimeTotal(mesZengguo.getWinccUpdateTime());
                            preRecord.setPhaseEndWinccId(mesZengguo.getWinccUpdatId());
                            if (preRecord.getPhaseStartTimeTotal() != null && preRecord.getPhaseEndTimeTotal() != null) {
                                long duration = preRecord.getPhaseEndTimeTotal().getTime() - preRecord.getPhaseStartTimeTotal().getTime();
                                preRecord.setPhaseDuration((float) (duration / 60000));
                            }

                            setPrerecordNormalInfo(preRecord, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);


                            mesZengguoRecordList.add(preRecord);
                        } else {
                            if (rawUpdateTime != null) {
                                List<MesZengguoRecord> preRecords = dataManager.load(MesZengguoRecord.class)
                                        .query("select e from MesZengguoRecord e " +
                                                "where e.mesZengguo = :mesZengguo " +
                                                "and e.phaseStartTimeTotal <= :phaseStartTimeTotal " +
                                                "and e.phaseEndTimeTotal is null " +
                                                "order by e.phaseStartTimeTotal desc")
                                        .parameter("mesZengguo", mesZengguo)
                                        .parameter("phaseStartTimeTotal", rawUpdateTime)
                                        .maxResults(1)
                                        .list();
                                if (!preRecords.isEmpty()) {
                                    MesZengguoRecord preRecordInDB = preRecords.getFirst();
                                    preRecordInDB.setPhaseEndTimeTotal(winccUpdateTime);
                                    preRecordInDB.setPhaseEndWinccId(winccId);
                                    if (preRecordInDB.getPhaseStartTimeTotal() != null && preRecordInDB.getPhaseEndTimeTotal() != null) {
                                        long duration = preRecordInDB.getPhaseEndTimeTotal().getTime() - preRecordInDB.getPhaseStartTimeTotal().getTime();
                                        preRecordInDB.setPhaseDuration((float) (duration / 60000));
                                    }
                                    setPrerecordNormalInfo(preRecordInDB, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
                                    mesZengguoRecordList.add(preRecordInDB);
                                }
                            }
                        }
                    }
                }
            }
            jobSaveDataService.saveZengguoData(jobConfig, areaZengguoList, mesZengguoRecordList,mesZengguoOperationList,mesZengguoUnitProcedureList, maxWinccId);
        }
    }

    private void finishPreOperation(List<MesZengguoOperation> mesZengguoOperationList, MesZengguo mesZengguo,EnumZengguoMainPhase mainPhase, MesZengguoRecord record,
                                    Date rawStartTimeTotal, Date rawShangzengStartTime, Date rawKagaiTime, Date rawLiujiuEndTime, Date rawEndTimeTotal,
                                    MesJiaochi rawJiaochiDown, Date rawJiaochiDownTime, Integer rawJiaochiDownLayer, EnumZaopeiType rawZaopeiTypeDown,
                                    float rawRunliangWaterAddDown, float rawRunliangDurationDown, float rawZaopeiQtyDown, float rawDaokeQtyDown, float rawLiangshiQtyDown,
                                    EnumLiangshiType rawLiangshiTypeDown, MesJiaochi rawJiaochiUp, Date rawJiaochiUpTime, Integer rawJiaochiUpLayer,
                                    EnumZaopeiType rawZaopeiTypeUp, float rawRunliangWaterAddUp, float rawRunliangDurationUp, float rawZaopeiQtyUp, float rawDaokeQtyUp,
                                    float rawLiangshiQtyUp, EnumLiangshiType rawLiangshiTypeUp, MesZenggouPhaseConfig rawPhase, EnumZaopeiType rawZaopeiType,
                                    Integer rawShangzengLayer, float rawShangzengDuration, float rawShangzengHeight, float rawJiejiuFirstClassDuration,
                                    float rawJiejiuSecondClassDuration, float rawJiejiuThirdClassDuration, float rawJiejiuDurationFeishui, float rawJiejiuDurationJiuwei,
                                    float rawLiangshuiAddQty, float rawHuishoudiguoWaterAddQty, float rawHotWaterAddQty, float rawJiuweiAddQty, float rawHuangshuiAddQty,
                                    Integer rawZengSequence, float rawEnergyQiShangzeng, float rawEnergyQiZhengliu, float rawLiujiuAddZhengzhuDuration) {
        MesZengguoOperation mesZengguoOperation = mesZengguoOperationList.stream()
                .filter(e -> e.getMesZengguo().equals(mesZengguo)
                        && e.getMainPhase().equals(mainPhase)
                        && e.getZengSequence().equals(rawZengSequence)
                        && e.getPhaseEndTimeTotal() == null)
                .max(Comparator.comparing(MesZengguoOperation::getPhaseStartTimeTotal))
                .orElse(null);
        if(mesZengguoOperation != null){
            setOperationEndInfo(record, mesZengguoOperation);
            setOperationNormalInfo(mesZengguoOperation, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
        }else {
            List<MesZengguoOperation> mesZengguoOperationList1 = dataManager.load(MesZengguoOperation.class)
                    .query("select e from MesZengguoOperation e " +
                            "where e.mesZengguo = :mesZengguo " +
                            "and e.zengSequence =:zengSequence " +
                            "and e.mainPhase = :mainPhase " +
                            "and e.phaseEndTimeTotal is null " +
                            "order by e.phaseStartTimeTotal desc")
                    .parameter("mesZengguo", mesZengguo)
                    .parameter("zengSequence", rawZengSequence)
                    .parameter("mainPhase", mainPhase)
                    .maxResults(1)
                    .list();
            if (!mesZengguoOperationList1.isEmpty()) {
                mesZengguoOperation = mesZengguoOperationList1.getFirst();
                setOperationEndInfo(record, mesZengguoOperation);
                setOperationNormalInfo(mesZengguoOperation, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
                mesZengguoOperationList.add(mesZengguoOperation);
            }
        }
    }



    private static void setOperationEndInfo(MesZengguoRecord record, MesZengguoOperation mesZengguoOperation) {
        mesZengguoOperation.setPhaseEndTimeTotal(record.getPhaseStartTimeTotal());
        mesZengguoOperation.setPhaseEndWinccId(record.getPhaseStartWinccId());
        if (mesZengguoOperation.getPhaseStartTimeTotal() != null && mesZengguoOperation.getPhaseEndTimeTotal() != null) {
            long duration = mesZengguoOperation.getPhaseEndTimeTotal().getTime() - mesZengguoOperation.getPhaseStartTimeTotal().getTime();
            mesZengguoOperation.setPhaseDuration((float) (duration / 60000));
        }
    }

    private void createNewOperation(List<MesZengguoOperation> mesZengguoOperationList, MesZengguo mesZengguo, EnumZengguoMainPhase mainPhase, MesZengguoRecord record) {
        MesZengguoOperation zengguoOperation = mesZengguoOperationList.stream()
                .filter(e -> e.getMesZengguo().equals(mesZengguo) && e.getMainPhase().equals(mainPhase)
                        &&e.getZengSequence().equals(record.getZengSequence()) && e.getPhaseEndTimeTotal() == null)
                .max(Comparator.comparing(MesZengguoOperation::getPhaseStartTimeTotal))
                .orElse(null);
        if (zengguoOperation == null) {
            zengguoOperation = dataManager.load(MesZengguoOperation.class)
                    .query("select e from MesZengguoOperation e " +
                            "where e.mesZengguo = :mesZengguo " +
                            "and e.mainPhase = :mainPhase " +
                            "and e.zengSequence = :zengSequence " +
                            "and e.phaseEndTimeTotal is null " +
                            "order by e.phaseStartTimeTotal desc")
                    .parameter("mainPhase", mainPhase)
                    .parameter("zengSequence", record.getZengSequence())
                    .parameter("mesZengguo", mesZengguo)
                    .maxResults(1)
                    .optional().orElse(null);
        }
        if (zengguoOperation == null) {
            zengguoOperation = dataManager.create(MesZengguoOperation.class);
            zengguoOperation.setMesZengguo(record.getMesZengguo());
            zengguoOperation.setPhaseStartTimeTotal(record.getPhaseStartTimeTotal());
            zengguoOperation.setPhaseStartWinccId(record.getPhaseStartWinccId());
            zengguoOperation.setZengguoPhase(record.getZengguoPhase());
            zengguoOperation.setMainPhase(mainPhase);
            setNewOperationNormalInfo(zengguoOperation, record);
            mesZengguoOperationList.add(zengguoOperation);
        }
    }

    private void setNewOperationNormalInfo(MesZengguoOperation zengguoOperation, MesZengguoRecord record) {
        setOperationNormalInfo(zengguoOperation, record.getStartTimeTotal(),record.getStartTimeDeviceShangZeng(),record.getStartTimeKagai(),record.getEndTimeLiujiu(),record.getEndTimeTall(),record.getJiaochiDown(),record.getJiaochiTimeDown(),record.getJiaochiLayerDown(),record.getZaopeiTypeDown(),record.getRunliangAddWaterDown(),record.getRunliangDurationDown(),record.getZaopeiQtyDown(),record.getDaokeQtyDown(),record.getLiangshiQtyDown(),record.getLiangshiTypeDown(),record.getJiaochiUp(),record.getJiaochiTimeUp(),record.getJiaochiLayerUp(),record.getZaopeiTypeUp(),record.getRunliangAddWaterUp(),record.getRunliangDurationUp(),record.getZaopeiQtyUp(),record.getDaokeQtyUp(),record.getLiangshiQtyUp(),record.getLiangshiTypUp(),record.getZengguoPhase(),record.getZaopeiType(),record.getShangzengLayer(),record.getShangzengDuration(),record.getShangzengHeight(),record.getJiejiuDurationFirstClass(),record.getJiejiuDurationSecondClass(),record.getJiejiuDurationThirdClass(),record.getJiejiuDurationFeishui(),record.getJiejiuDurationJiuwei(),record.getLiangshuiAddQty(),record.getHuishoudiguoWaterAddQty(),record.getHotWaterAddQty(),record.getJiuweiAddQty(),record.getHuangshuiAddQty(),record.getZengSequence(),record.getEnergyQiShangzeng(),record.getEnergyQiZhengliu(),record.getLiujiuAddZhengzhuDuration());
    }



    private static void setPrerecordNormalInfo(MesZengguoRecord preRecord, Date rawStartTimeTotal, Date rawShangzengStartTime, Date rawKagaiTime, Date rawLiujiuEndTime, Date rawEndTimeTotal, MesJiaochi rawJiaochiDown, Date rawJiaochiDownTime, Integer rawJiaochiDownLayer, EnumZaopeiType rawZaopeiTypeDown, float rawRunliangWaterAddDown, float rawRunliangDurationDown, float rawZaopeiQtyDown, float rawDaokeQtyDown, float rawLiangshiQtyDown, EnumLiangshiType rawLiangshiTypeDown, MesJiaochi rawJiaochiUp, Date rawJiaochiUpTime, Integer rawJiaochiUpLayer, EnumZaopeiType rawZaopeiTypeUp, float rawRunliangWaterAddUp, float rawRunliangDurationUp, float rawZaopeiQtyUp, float rawDaokeQtyUp, float rawLiangshiQtyUp, EnumLiangshiType rawLiangshiTypeUp, MesZenggouPhaseConfig rawPhase, EnumZaopeiType rawZaopeiType, Integer rawShangzengLayer, float rawShangzengDuration, float rawShangzengHeight, float rawJiejiuFirstClassDuration, float rawJiejiuSecondClassDuration, float rawJiejiuThirdClassDuration, float rawJiejiuDurationFeishui, float rawJiejiuDurationJiuwei, float rawLiangshuiAddQty, float rawHuishoudiguoWaterAddQty, float rawHotWaterAddQty, float rawJiuweiAddQty, float rawHuangshuiAddQty, Integer rawZengSequence, float rawEnergyQiShangzeng, float rawEnergyQiZhengliu, float rawLiujiuAddZhengzhuDuration) {
        preRecord.setStartTimeTotal(rawStartTimeTotal);
        preRecord.setStartTimeDeviceShangZeng(rawShangzengStartTime);
        preRecord.setStartTimeKagai(rawKagaiTime);
        preRecord.setEndTimeLiujiu(rawLiujiuEndTime);
        preRecord.setEndTimeTall(rawEndTimeTotal);
        preRecord.setJiaochiDown(rawJiaochiDown);
        preRecord.setJiaochiTimeDown(rawJiaochiDownTime);
        preRecord.setJiaochiLayerDown(rawJiaochiDownLayer);
        preRecord.setZaopeiTypeDown(rawZaopeiTypeDown);
        preRecord.setRunliangAddWaterDown(rawRunliangWaterAddDown);
        preRecord.setRunliangDurationDown(rawRunliangDurationDown);
        preRecord.setZaopeiQtyDown(rawZaopeiQtyDown);
        preRecord.setDaokeQtyDown(rawDaokeQtyDown);
        preRecord.setLiangshiQtyDown(rawLiangshiQtyDown);
        preRecord.setLiangshiTypeDown(rawLiangshiTypeDown);
        preRecord.setJiaochiUp(rawJiaochiUp);
        preRecord.setJiaochiTimeUp(rawJiaochiUpTime);
        preRecord.setJiaochiLayerUp(rawJiaochiUpLayer);
        preRecord.setZaopeiTypeUp(rawZaopeiTypeUp);
        preRecord.setRunliangAddWaterUp(rawRunliangWaterAddUp);
        preRecord.setRunliangDurationUp(rawRunliangDurationUp);
        preRecord.setZaopeiQtyUp(rawZaopeiQtyUp);
        preRecord.setDaokeQtyUp(rawDaokeQtyUp);
        preRecord.setLiangshiQtyUp(rawLiangshiQtyUp);
        preRecord.setLiangshiTypUp(rawLiangshiTypeUp);
        preRecord.setZengguoPhase(rawPhase);
        preRecord.setZaopeiType(rawZaopeiType);
        preRecord.setShangzengLayer(rawShangzengLayer);
        preRecord.setShangzengDuration(rawShangzengDuration);
        preRecord.setShangzengHeight(rawShangzengHeight);
        preRecord.setJiejiuDurationFirstClass(rawJiejiuFirstClassDuration);
        preRecord.setJiejiuDurationSecondClass(rawJiejiuSecondClassDuration);
        preRecord.setJiejiuDurationThirdClass(rawJiejiuThirdClassDuration);
        preRecord.setJiejiuDurationFeishui(rawJiejiuDurationFeishui);
        preRecord.setJiejiuDurationJiuwei(rawJiejiuDurationJiuwei);
        preRecord.setLiangshuiAddQty(rawLiangshuiAddQty);
        preRecord.setHuishoudiguoWaterAddQty(rawHuishoudiguoWaterAddQty);
        preRecord.setHotWaterAddQty(rawHotWaterAddQty);
        preRecord.setJiuweiAddQty(rawJiuweiAddQty);
        preRecord.setHuangshuiAddQty(rawHuangshuiAddQty);
        preRecord.setZengSequence(rawZengSequence);
        preRecord.setEnergyQiShangzeng(rawEnergyQiShangzeng);
        preRecord.setEnergyQiZhengliu(rawEnergyQiZhengliu);
        preRecord.setLiujiuAddZhengzhuDuration(rawLiujiuAddZhengzhuDuration);
    }



    private static void setNewRecordNormalInfo(MesZengguoRecord record, MesZengguo mesZengguo) {
        setPrerecordNormalInfo(record, mesZengguo.getStartTimeTotal(), mesZengguo.getStartTimeDeviceShangZeng(), mesZengguo.getStartTimeKagai(), mesZengguo.getEndTimeLiujiu(), mesZengguo.getEndTimeTall(), mesZengguo.getJiaochiDown(), mesZengguo.getJiaochiTimeDown(), mesZengguo.getJiaochiLayerDown(), mesZengguo.getZaopeiTypeDown(), mesZengguo.getRunliangAddWaterDown(), mesZengguo.getRunliangDurationDown(), mesZengguo.getZaopeiQtyDown(), mesZengguo.getDaokeQtyDown(), mesZengguo.getLiangshiQtyDown(), mesZengguo.getLiangshiTypeDown(), mesZengguo.getJiaochiUp(), mesZengguo.getJiaochiTimeUp(), mesZengguo.getJiaochiLayerUp(), mesZengguo.getZaopeiTypeUp(), mesZengguo.getRunliangAddWaterUp(), mesZengguo.getRunliangDurationUp(), mesZengguo.getZaopeiQtyUp(), mesZengguo.getDaokeQtyUp(), mesZengguo.getLiangshiQtyUp(), mesZengguo.getLiangshiTypUp(), mesZengguo.getZengguoPhase(), mesZengguo.getZaopeiType(), mesZengguo.getShangzengLayer(), mesZengguo.getShangzengDuration(), mesZengguo.getShangzengHeight(), mesZengguo.getJiejiuDurationFirstClass(), mesZengguo.getJiejiuDurationSecondClass(), mesZengguo.getJiejiuDurationThirdClass(), mesZengguo.getJiejiuDurationFeishui(), mesZengguo.getJiejiuDurationJiuwei(), mesZengguo.getLiangshuiAddQty(), mesZengguo.getHuishoudiguoWaterAddQty(), mesZengguo.getHotWaterAddQty(), mesZengguo.getJiuweiAddQty(), mesZengguo.getHuangshuiAddQty(), mesZengguo.getZengSequence(), mesZengguo.getEnergyQiShangzeng(), mesZengguo.getEnergyQiZhengliu(), mesZengguo.getLiujiuAddZhengzhuDuration());
    }

    private static void setOperationNormalInfo(MesZengguoOperation operation,Date rawStartTimeTotal, Date rawShangzengStartTime, Date rawKagaiTime, Date rawLiujiuEndTime, Date rawEndTimeTotal, MesJiaochi rawJiaochiDown, Date rawJiaochiDownTime, Integer rawJiaochiDownLayer, EnumZaopeiType rawZaopeiTypeDown, float rawRunliangWaterAddDown, float rawRunliangDurationDown, float rawZaopeiQtyDown, float rawDaokeQtyDown, float rawLiangshiQtyDown, EnumLiangshiType rawLiangshiTypeDown, MesJiaochi rawJiaochiUp, Date rawJiaochiUpTime, Integer rawJiaochiUpLayer, EnumZaopeiType rawZaopeiTypeUp, float rawRunliangWaterAddUp, float rawRunliangDurationUp, float rawZaopeiQtyUp, float rawDaokeQtyUp, float rawLiangshiQtyUp, EnumLiangshiType rawLiangshiTypeUp, MesZenggouPhaseConfig rawPhase, EnumZaopeiType rawZaopeiType, Integer rawShangzengLayer, float rawShangzengDuration, float rawShangzengHeight, float rawJiejiuFirstClassDuration, float rawJiejiuSecondClassDuration, float rawJiejiuThirdClassDuration, float rawJiejiuDurationFeishui, float rawJiejiuDurationJiuwei, float rawLiangshuiAddQty, float rawHuishoudiguoWaterAddQty, float rawHotWaterAddQty, float rawJiuweiAddQty, float rawHuangshuiAddQty, Integer rawZengSequence, float rawEnergyQiShangzeng, float rawEnergyQiZhengliu, float rawLiujiuAddZhengzhuDuration) {
        operation.setZengguoPhase(rawPhase);

        operation.setStartTimeTotal(rawStartTimeTotal);
        operation.setStartTimeDeviceShangZeng(rawShangzengStartTime);
        operation.setStartTimeKagai(rawKagaiTime);
        operation.setEndTimeLiujiu(rawLiujiuEndTime);
        operation.setEndTimeTall(rawEndTimeTotal);
        operation.setJiaochiDown(rawJiaochiDown);
        operation.setJiaochiTimeDown(rawJiaochiDownTime);
        operation.setJiaochiLayerDown(rawJiaochiDownLayer);
        operation.setZaopeiTypeDown(rawZaopeiTypeDown);
        operation.setRunliangAddWaterDown(rawRunliangWaterAddDown);
        operation.setRunliangDurationDown(rawRunliangDurationDown);
        operation.setZaopeiQtyDown(rawZaopeiQtyDown);
        operation.setDaokeQtyDown(rawDaokeQtyDown);
        operation.setLiangshiQtyDown(rawLiangshiQtyDown);
        operation.setLiangshiTypeDown(rawLiangshiTypeDown);
        operation.setJiaochiUp(rawJiaochiUp);
        operation.setJiaochiTimeUp(rawJiaochiUpTime);
        operation.setJiaochiLayerUp(rawJiaochiUpLayer);
        operation.setZaopeiTypeUp(rawZaopeiTypeUp);
        operation.setRunliangAddWaterUp(rawRunliangWaterAddUp);
        operation.setRunliangDurationUp(rawRunliangDurationUp);
        operation.setZaopeiQtyUp(rawZaopeiQtyUp);
        operation.setDaokeQtyUp(rawDaokeQtyUp);
        operation.setLiangshiQtyUp(rawLiangshiQtyUp);
        operation.setLiangshiTypUp(rawLiangshiTypeUp);

        operation.setZaopeiType(rawZaopeiType);
        operation.setShangzengLayer(rawShangzengLayer);
        operation.setShangzengDuration(rawShangzengDuration);
        operation.setShangzengHeight(rawShangzengHeight);
        operation.setJiejiuDurationFirstClass(rawJiejiuFirstClassDuration);
        operation.setJiejiuDurationSecondClass(rawJiejiuSecondClassDuration);
        operation.setJiejiuDurationThirdClass(rawJiejiuThirdClassDuration);
        operation.setJiejiuDurationFeishui(rawJiejiuDurationFeishui);
        operation.setJiejiuDurationJiuwei(rawJiejiuDurationJiuwei);
        operation.setLiangshuiAddQty(rawLiangshuiAddQty);
        operation.setHuishoudiguoWaterAddQty(rawHuishoudiguoWaterAddQty);
        operation.setHotWaterAddQty(rawHotWaterAddQty);
        operation.setJiuweiAddQty(rawJiuweiAddQty);
        operation.setHuangshuiAddQty(rawHuangshuiAddQty);
        operation.setZengSequence(rawZengSequence);
        operation.setEnergyQiShangzeng(rawEnergyQiShangzeng);
        operation.setEnergyQiZhengliu(rawEnergyQiZhengliu);
        operation.setLiujiuAddZhengzhuDuration(rawLiujiuAddZhengzhuDuration);
    }

    private void setNewUnitProcedureNormalInfo(MesZengguoUnitProcedure unitProcedure, MesZengguoRecord record) {
        setUnitProcedureNormalInfo(unitProcedure, record.getStartTimeTotal(),record.getStartTimeDeviceShangZeng(),record.getStartTimeKagai(),record.getEndTimeLiujiu(),record.getEndTimeTall(),record.getJiaochiDown(),record.getJiaochiTimeDown(),record.getJiaochiLayerDown(),record.getZaopeiTypeDown(),record.getRunliangAddWaterDown(),record.getRunliangDurationDown(),record.getZaopeiQtyDown(),record.getDaokeQtyDown(),record.getLiangshiQtyDown(),record.getLiangshiTypeDown(),record.getJiaochiUp(),record.getJiaochiTimeUp(),record.getJiaochiLayerUp(),record.getZaopeiTypeUp(),record.getRunliangAddWaterUp(),record.getRunliangDurationUp(),record.getZaopeiQtyUp(),record.getDaokeQtyUp(),record.getLiangshiQtyUp(),record.getLiangshiTypUp(),record.getZengguoPhase(),record.getZaopeiType(),record.getShangzengLayer(),record.getShangzengDuration(),record.getShangzengHeight(),record.getJiejiuDurationFirstClass(),record.getJiejiuDurationSecondClass(),record.getJiejiuDurationThirdClass(),record.getJiejiuDurationFeishui(),record.getJiejiuDurationJiuwei(),record.getLiangshuiAddQty(),record.getHuishoudiguoWaterAddQty(),record.getHotWaterAddQty(),record.getJiuweiAddQty(),record.getHuangshuiAddQty(),record.getZengSequence(),record.getEnergyQiShangzeng(),record.getEnergyQiZhengliu(),record.getLiujiuAddZhengzhuDuration());
    }


    private static void setUnitProcedureNormalInfo(MesZengguoUnitProcedure unitProcedure,Date rawStartTimeTotal, Date rawShangzengStartTime, Date rawKagaiTime, Date rawLiujiuEndTime, Date rawEndTimeTotal, MesJiaochi rawJiaochiDown, Date rawJiaochiDownTime, Integer rawJiaochiDownLayer, EnumZaopeiType rawZaopeiTypeDown, float rawRunliangWaterAddDown, float rawRunliangDurationDown, float rawZaopeiQtyDown, float rawDaokeQtyDown, float rawLiangshiQtyDown, EnumLiangshiType rawLiangshiTypeDown, MesJiaochi rawJiaochiUp, Date rawJiaochiUpTime, Integer rawJiaochiUpLayer, EnumZaopeiType rawZaopeiTypeUp, float rawRunliangWaterAddUp, float rawRunliangDurationUp, float rawZaopeiQtyUp, float rawDaokeQtyUp, float rawLiangshiQtyUp, EnumLiangshiType rawLiangshiTypeUp, MesZenggouPhaseConfig rawPhase, EnumZaopeiType rawZaopeiType, Integer rawShangzengLayer, float rawShangzengDuration, float rawShangzengHeight, float rawJiejiuFirstClassDuration, float rawJiejiuSecondClassDuration, float rawJiejiuThirdClassDuration, float rawJiejiuDurationFeishui, float rawJiejiuDurationJiuwei, float rawLiangshuiAddQty, float rawHuishoudiguoWaterAddQty, float rawHotWaterAddQty, float rawJiuweiAddQty, float rawHuangshuiAddQty, Integer rawZengSequence, float rawEnergyQiShangzeng, float rawEnergyQiZhengliu, float rawLiujiuAddZhengzhuDuration) {
        unitProcedure.setZengguoPhase(rawPhase);
        unitProcedure.setStartTimeTotal(rawStartTimeTotal);
        unitProcedure.setStartTimeDeviceShangZeng(rawShangzengStartTime);
        unitProcedure.setStartTimeKagai(rawKagaiTime);
        unitProcedure.setEndTimeLiujiu(rawLiujiuEndTime);
        unitProcedure.setEndTimeTall(rawEndTimeTotal);
        unitProcedure.setJiaochiDown(rawJiaochiDown);
        unitProcedure.setJiaochiTimeDown(rawJiaochiDownTime);
        unitProcedure.setJiaochiLayerDown(rawJiaochiDownLayer);
        unitProcedure.setZaopeiTypeDown(rawZaopeiTypeDown);
        unitProcedure.setRunliangAddWaterDown(rawRunliangWaterAddDown);
        unitProcedure.setRunliangDurationDown(rawRunliangDurationDown);
        unitProcedure.setZaopeiQtyDown(rawZaopeiQtyDown);
        unitProcedure.setDaokeQtyDown(rawDaokeQtyDown);
        unitProcedure.setLiangshiQtyDown(rawLiangshiQtyDown);
        unitProcedure.setLiangshiTypeDown(rawLiangshiTypeDown);
        unitProcedure.setJiaochiUp(rawJiaochiUp);
        unitProcedure.setJiaochiTimeUp(rawJiaochiUpTime);
        unitProcedure.setJiaochiLayerUp(rawJiaochiUpLayer);
        unitProcedure.setZaopeiTypeUp(rawZaopeiTypeUp);
        unitProcedure.setRunliangAddWaterUp(rawRunliangWaterAddUp);
        unitProcedure.setRunliangDurationUp(rawRunliangDurationUp);
        unitProcedure.setZaopeiQtyUp(rawZaopeiQtyUp);
        unitProcedure.setDaokeQtyUp(rawDaokeQtyUp);
        unitProcedure.setLiangshiQtyUp(rawLiangshiQtyUp);
        unitProcedure.setLiangshiTypUp(rawLiangshiTypeUp);

        unitProcedure.setZaopeiType(rawZaopeiType);
        unitProcedure.setShangzengLayer(rawShangzengLayer);
        unitProcedure.setShangzengDuration(rawShangzengDuration);
        unitProcedure.setShangzengHeight(rawShangzengHeight);
        unitProcedure.setJiejiuDurationFirstClass(rawJiejiuFirstClassDuration);
        unitProcedure.setJiejiuDurationSecondClass(rawJiejiuSecondClassDuration);
        unitProcedure.setJiejiuDurationThirdClass(rawJiejiuThirdClassDuration);
        unitProcedure.setJiejiuDurationFeishui(rawJiejiuDurationFeishui);
        unitProcedure.setJiejiuDurationJiuwei(rawJiejiuDurationJiuwei);
        unitProcedure.setLiangshuiAddQty(rawLiangshuiAddQty);
        unitProcedure.setHuishoudiguoWaterAddQty(rawHuishoudiguoWaterAddQty);
        unitProcedure.setHotWaterAddQty(rawHotWaterAddQty);
        unitProcedure.setJiuweiAddQty(rawJiuweiAddQty);
        unitProcedure.setHuangshuiAddQty(rawHuangshuiAddQty);
        unitProcedure.setZengSequence(rawZengSequence);
        unitProcedure.setEnergyQiShangzeng(rawEnergyQiShangzeng);
        unitProcedure.setEnergyQiZhengliu(rawEnergyQiZhengliu);
        unitProcedure.setLiujiuAddZhengzhuDuration(rawLiujiuAddZhengzhuDuration);
    }

    /**
     * 检查状态是否需要创建甑锅斗操作记录
     * 200：加底锅水Start——————甑锅任务开始
     *                         201：打黄水
     *                         202：打酒尾
     *                         203：打底锅回收水
     *                         204：打热水
     *                         205：加底锅水End
     *                         300：等待转位输送机到位
     *                         4**：上甑——————机器人上甑开始
     */
//                        if(record.getZengguoPhase() != null && record.getZengguoPhase().getPhaseNo() >= 200 && record.getZengguoPhase().getPhaseNo() < 300){
//                            createNewOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.DIGUOSHUI_ADD, record);
//                        }
//                        /**
//                         * 4**：上甑——————机器人上甑开始
//                         *             0、上甑启动
//                         *             1、"Home点",
//                         *             2、"Home点运行至接料点",
//                         *             3、"接料中",
//                         *             4、"接料点运行至待汽点",
//                         *             5、"待汽中",
//                         *             6、"内圈铺料中",
//                         *             7、"中圈铺料中",
//                         *             8、"外圈铺料中",
//                         *             9、"单层铺料完成回接料点",
//                         * （--------3...9循环，直至满甑--------）
//                         *            10、"清扫甑边",
//                         *            11、"装甑结束回Home点",
//                         * 499：等待转位输送机归零位
//                         * 500：馏酒Start————————卡盘馏酒开始
//                         */
//                        if(record.getZengguoPhase() != null && record.getZengguoPhase().getPhaseNo() >= 400 && record.getZengguoPhase().getPhaseNo() < 500){
//                            createNewOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.SHANGZENG, record);
//
//                            finishPreOperation(mesZengguoOperationList, mesZengguo,EnumZengguoMainPhase.DIGUOSHUI_ADD, record, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
//                        }
//
//                        /**
//                         * 500：馏酒Start————————卡盘馏酒开始
//                         * 510：馏酒-合盖
//                         * 520、馏酒（酒头）
//                         * 521、馏酒（一级）
//                         * 522、馏酒（二级）
//                         * 523、馏酒（三级）
//                         * 524、馏酒（酒尾）
//                         * 525、蒸煮（冲酸）——————馏酒结束
//                         */
//
//                        if(record.getZengguoPhase() != null && record.getZengguoPhase().getPhaseNo() >= 500 && record.getZengguoPhase().getPhaseNo() < 525){
//                            createNewOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.LIUJIU, record);
//
//                            finishPreOperation(mesZengguoOperationList, mesZengguo,EnumZengguoMainPhase.SHANGZENG, record, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
//                        }
//
//                        /**
//                         * 525、蒸煮（冲酸）——————馏酒结束
//                         * 526、打量水1（现有工艺取消）
//                         * 527、打量水2
//                         * 528、焖料
//                         * 529、圆气
//                         * 530：抽真空、降温
//                         */
//                        if(record.getZengguoPhase() != null && record.getZengguoPhase().getPhaseNo() >= 525 && record.getZengguoPhase().getPhaseNo() < 530) {
//                            createNewOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.ZHENGZHU_CHONGSUAN, record);
//
//                            finishPreOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.LIUJIU, record, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
//                        }
//
//                        /**
//                         * 530：抽真空、降温
//                         * 540：起盖
//                         * 549：待407到位
//                         * 550：倒桶
//                         * 560：蒸馏End
//                         * 600：甑桶回正
//                         * 700：End——————————甑任务结束
//                         * 0：空闲
//                         */
//                        if(record.getZengguoPhase() != null && record.getZengguoPhase().getPhaseNo() >= 530 && record.getZengguoPhase().getPhaseNo() < 700) {
//                            createNewOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.POST_DEAL, record);
//
//                            finishPreOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.ZHENGZHU_CHONGSUAN, record, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
//                        }
//
//                        /**
//                         * 700：End——————————甑任务结束
//                         * 0：空闲
//                         */
//                        if(record.getZengguoPhase() != null && (record.getZengguoPhase().getPhaseNo() == 700 || record.getZengguoPhase().getPhaseNo() == 0)) {
//                            finishPreOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.LIUJIU, record, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
//
//                            finishPreOperation(mesZengguoOperationList, mesZengguo, EnumZengguoMainPhase.POST_DEAL, record, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
//                        }
//
//                        /**
//                         * 创建甑锅主阶段
//                         *
//                         */
//                        if(record.getZengguoPhase() != null && record.getZengguoPhase().getPhaseNo() >= 200 && record.getZengguoPhase().getPhaseNo() <= 524){
//
//                            MesZengguoUnitProcedure mesZengguoUnitProcedure = mesZengguoUnitProcedureList.stream()
//                                    .filter(e -> e.getMesZengguo().equals(mesZengguo)
//                                            && e.getZengSequence().equals(record.getZengSequence())
//                                            && e.getPhaseEndTimeTotal() == null)
//                                    .max(Comparator.comparing(MesZengguoUnitProcedure::getPhaseStartTimeTotal))
//                                    .orElse(null);
//                            if(mesZengguoUnitProcedure == null){
//                                List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList1 = dataManager.load(MesZengguoUnitProcedure.class)
//                                        .query("select e from MesZengguoUnitProcedure e " +
//                                                "where e.mesZengguo = :mesZengguo " +
//                                                "and e.zengSequence = :zengSequence " +
//                                                "and e.phaseEndTimeTotal is null " +
//                                                "order by e.phaseStartTimeTotal desc")
//                                        .parameter("mesZengguo", mesZengguo)
//                                        .parameter("zengSequence",record.getZengSequence())
//                                        .maxResults(1)
//                                        .list();
//                                if(mesZengguoUnitProcedureList1.isEmpty()){
//                                    mesZengguoUnitProcedure = dataManager.create(MesZengguoUnitProcedure.class);
//                                    mesZengguoUnitProcedure.setMesZengguo(mesZengguo);
//                                    mesZengguoUnitProcedure.setPhaseStartTimeTotal(winccUpdateTime);
//                                    mesZengguoUnitProcedure.setPhaseStartWinccId(winccId);
//                                    setNewUnitProcedureNormalInfo(mesZengguoUnitProcedure, record);
//                                    mesZengguoUnitProcedureList.add(mesZengguoUnitProcedure);
//                                }
//                            }
//                        }
//
//                        /**
//                         * 结束主阶段
//                         */
//
//                        if(record.getZengguoPhase() != null && (record.getZengguoPhase().getPhaseNo() == 700 ||record.getZengguoPhase().getPhaseNo() == 0)){
//                            MesZengguoUnitProcedure mesZengguoUnitProcedure = mesZengguoUnitProcedureList.stream()
//                                    .filter(e -> e.getMesZengguo().equals(mesZengguo)
//                                            && e.getZengSequence().equals(rawZengSequence)
//                                            && e.getPhaseEndTimeTotal() == null)
//                                    .max(Comparator.comparing(MesZengguoUnitProcedure::getPhaseStartTimeTotal))
//                                    .orElse(null);
//                            if(mesZengguoUnitProcedure == null){
//                                List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList1 = dataManager.load(MesZengguoUnitProcedure.class)
//                                        .query("select e from MesZengguoUnitProcedure e " +
//                                                "where e.mesZengguo = :mesZengguo " +
//                                                "and e.zengSequence = :zengSequence " +
//                                                "and e.phaseEndTimeTotal is null " +
//                                                "order by e.phaseStartTimeTotal desc")
//                                        .parameter("mesZengguo", mesZengguo)
//                                        .parameter("zengSequence", rawZengSequence)
//                                        .maxResults(1)
//                                        .list();
//                                if(!mesZengguoUnitProcedureList1.isEmpty()) {
//                                    mesZengguoUnitProcedure = mesZengguoUnitProcedureList1.getFirst();
//                                    mesZengguoUnitProcedureList.add(mesZengguoUnitProcedure);
//                                }
//                            }
//                            if(mesZengguoUnitProcedure != null){
//                                mesZengguoUnitProcedure.setMesZengguo(mesZengguo);
//                                mesZengguoUnitProcedure.setPhaseEndTimeTotal(winccUpdateTime);
//                                mesZengguoUnitProcedure.setPhaseEndWinccId(winccId);
//                                if(mesZengguoUnitProcedure.getPhaseStartTimeTotal() != null && mesZengguoUnitProcedure.getPhaseEndTimeTotal() != null){
//                                    long duration = mesZengguoUnitProcedure.getPhaseEndTimeTotal().getTime() - mesZengguoUnitProcedure.getPhaseStartTimeTotal().getTime();
//                                    mesZengguoUnitProcedure.setPhaseDuration((float)(duration/60000));
//                                }
//                                setUnitProcedureNormalInfo(mesZengguoUnitProcedure, rawStartTimeTotal, rawShangzengStartTime, rawKagaiTime, rawLiujiuEndTime, rawEndTimeTotal, rawJiaochiDown, rawJiaochiDownTime, rawJiaochiDownLayer, rawZaopeiTypeDown, rawRunliangWaterAddDown, rawRunliangDurationDown, rawZaopeiQtyDown, rawDaokeQtyDown, rawLiangshiQtyDown, rawLiangshiTypeDown, rawJiaochiUp, rawJiaochiUpTime, rawJiaochiUpLayer, rawZaopeiTypeUp, rawRunliangWaterAddUp, rawRunliangDurationUp, rawZaopeiQtyUp, rawDaokeQtyUp, rawLiangshiQtyUp, rawLiangshiTypeUp, rawPhase, rawZaopeiType, rawShangzengLayer, rawShangzengDuration, rawShangzengHeight, rawJiejiuFirstClassDuration, rawJiejiuSecondClassDuration, rawJiejiuThirdClassDuration, rawJiejiuDurationFeishui, rawJiejiuDurationJiuwei, rawLiangshuiAddQty, rawHuishoudiguoWaterAddQty, rawHotWaterAddQty, rawJiuweiAddQty, rawHuangshuiAddQty, rawZengSequence, rawEnergyQiShangzeng, rawEnergyQiZhengliu, rawLiujiuAddZhengzhuDuration);
//                            }
//                        }

}
