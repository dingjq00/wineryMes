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

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/3 22:40
 */
@DisallowConcurrentExecution
public class ZhuanyundouJob implements Job {
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
                .parameter("mainPhase", EnumProcessMainPhase.ZHUANYUNDOU)
                .list();
        List<MesZhuanyundou> mesZhuanyundouList = dataManager.load(MesZhuanyundou.class)
                .query("select e from MesZhuanyundou e")
                .list();

        List<MesWinccItemConfig> mesWinccItemConfigList = dataManager.load(MesWinccItemConfig.class)
                .query("select e from MesWinccItemConfig e ")
                .list();

        List<MesJiaochi> mesJiaochiList = dataManager.load(MesJiaochi.class)
                .query("select e from MesJiaochi e ")
                .list();

        for (JobConfig jobConfig : jobConfigList) {
            List<MesZhuanyundouRecord> mesZhuanyundouRecordList = new ArrayList<>();
            MesArea mesArea = jobConfig.getMesArea();
            Integer areaCode = mesArea.getAreaCode();
            Integer currentWinccId = jobConfig.getWinccId();

            /*
             * 获取每个单元的转运斗
             */
            List<MesZhuanyundou> areaZhuanyundouList = mesZhuanyundouList.stream()
                    .filter(mesZhuanyundou -> mesZhuanyundou.getMesArea().equals(mesArea))
                    .sorted(Comparator.comparing(MesZhuanyundou::getZhuanyundouNo))
                    .toList();
            int areaZhuanyundouListSize = areaZhuanyundouList.size();
            if (areaZhuanyundouListSize == 0) {
                continue;
            }
            /**
             * 获取每个单元Wincc 转运斗待处理的数据
             */
            List<WinccZhuanyundou> winccZhuanyundouList = dataManager.load(WinccZhuanyundou.class)
                    .query("select e from WinccZhuanyundou e " +
                            "where e.areaNo = :areaCode and e.winccId > :currentWinccId " +
                            "order by e.winccId")
                    .parameter("areaCode", areaCode)
                    .parameter("currentWinccId", currentWinccId)
                    .maxResults(10000)
                    .list();
            if (winccZhuanyundouList.isEmpty()) {
                continue;
            }
            // 获取最大的winccId, 用于更新JobConfig的winccId
            Integer maxWinccId = winccZhuanyundouList.stream()
                    .map(WinccZhuanyundou::getWinccId)
                    .max(Integer::compareTo)
                    .orElse(0);

            /**
             * 处理Wincc 转运斗待处理的数据
             */
            for (WinccZhuanyundou winccZhuanyundou : winccZhuanyundouList) {
                Date winccUpdateTime = winccZhuanyundou.getStarttime();
                Integer winccId = winccZhuanyundou.getWinccId();
                String comment = winccZhuanyundou.getComment();
                // 如果comment 为null 或为空，不处理
                if (comment == null || comment.isEmpty()) {
                    continue;
                }
                // Split the comment string by commas
                String[] commentParts = comment.split(",");
                int propertyNumber = commentParts.length / areaZhuanyundouListSize;
                for (int i = 0; i < areaZhuanyundouListSize; i++) {
                    MesZhuanyundou mesZhuanyundou = areaZhuanyundouList.get(i);
                    String zhuanyundouName = mesZhuanyundou.getZhuanyundouName();
                    int startIndex = i * propertyNumber;
                    int endIndex = (i + 1) * propertyNumber;
                    String[] zhuanyundouParts = Arrays.copyOfRange(commentParts, startIndex, endIndex);
                    // wincc更新Id
                    Integer rawWinccId = mesZhuanyundou.getWinccUpdateId() == null ? 0 : mesZhuanyundou.getWinccUpdateId();
                    mesZhuanyundou.setWinccUpdateId(winccId);
                    // wincc更新时间
                    Date rawWinccUpdateTime = mesZhuanyundou.getWinccUpdateTime();
                    mesZhuanyundou.setWinccUpdateTime(winccUpdateTime);


                    // 料斗编码/编号	0
                    Integer rawWinccZhuanyundouNo = mesZhuanyundou.getZhuanyundouNo() == null ? 0 : mesZhuanyundou.getZhuanyundouNo();
                    Integer winccZhuanyundouNo = Integer.parseInt(zhuanyundouParts[0]);
                    mesZhuanyundou.setWincc_No(winccZhuanyundouNo);
                    //"该斗当前所在位置，（每次位置发生变化后，均记录到数据库中，便于后期追踪，但是如果考虑执行效率的话，这里可以只同步记录料斗No、料斗编码/编号、空斗/满斗、出窖糟/回窖糟）"	1
                    MesWinccItemConfig rawCurrentLocation = mesZhuanyundou.getCurrentLocation() == null ? null : mesZhuanyundou.getCurrentLocation();
                    Integer currentLocation = Integer.parseInt(zhuanyundouParts[1]);
                    MesWinccItemConfig mesWinccItemLocation = mesWinccItemConfigList.stream()
                            .filter(e -> e.getConfigType().equals(EnumMesWinccItemConfig.ZYD_LOCATION))
                            .filter(e -> e.getValueNo().equals(currentLocation))
                            .findFirst()
                            .orElse(null);
                    mesZhuanyundou.setCurrentLocation(mesWinccItemLocation);

                    //空斗/满斗	2
                    MesWinccItemConfig rawFullOrEmpty = mesZhuanyundou.getFullOrEmpty() == null ? null : mesZhuanyundou.getFullOrEmpty();
                    boolean emptyFull = Boolean.parseBoolean(zhuanyundouParts[2]);
                    Integer emptyFullNo = emptyFull ? 1 : 0;
                    MesWinccItemConfig mesWinccItemEmptyFull = mesWinccItemConfigList.stream()
                            .filter(e -> e.getConfigType().equals(EnumMesWinccItemConfig.EMPTY_FULL))
                            .filter(e -> e.getValueNo().equals(emptyFullNo))
                            .findFirst()
                            .orElse(null);
                    mesZhuanyundou.setFullOrEmpty(mesWinccItemEmptyFull);
                    //出窖糟/回窖糟	3
                    MesWinccItemConfig rawChujiaozaoOrHuijiaoZao = mesZhuanyundou.getChujiaozaoOrHuijiaoZao() == null ? null : mesZhuanyundou.getChujiaozaoOrHuijiaoZao();
                    boolean outOrBack = Boolean.parseBoolean(zhuanyundouParts[3]);
                    Integer outOrBackNo = outOrBack ? 1 : 0;
                    MesWinccItemConfig mesWinccItemOutBack = mesWinccItemConfigList.stream()
                            .filter(e -> e.getConfigType().equals(EnumMesWinccItemConfig.CHUJIAO_HUIJIAO))
                            .filter(e -> e.getValueNo().equals(outOrBackNo))
                            .findFirst()
                            .orElse(null);
                    mesZhuanyundou.setChujiaozaoOrHuijiaoZao(mesWinccItemOutBack);
                    //回窖糟：摊晾时长	4
                    Float rawTanliangDuration = mesZhuanyundou.getHuijiaoTanliangDuration() == null ? 0 : mesZhuanyundou.getHuijiaoTanliangDuration();
                    Float tanliangDuration = Float.parseFloat(zhuanyundouParts[4]);
                    mesZhuanyundou.setHuijiaoTanliangDuration(tanliangDuration);
                    //回窖糟：该糟醅源头—出窖层数	5
                    Integer rawOutLayer = mesZhuanyundou.getHuijiaoJiaochiLayer() == null ? 0 : mesZhuanyundou.getHuijiaoJiaochiLayer();
                    Integer outLayer = Integer.parseInt(zhuanyundouParts[5]);
                    mesZhuanyundou.setHuijiaoJiaochiLayer(outLayer);
                    //回窖糟：该糟醅源头—出窖窖池编号	6    // 窖池的需要进行处理，窖池编号如何进行设置的
                    MesJiaochi rawHuijiaoJiaochi = mesZhuanyundou.getHuijiaoJiaochi();
                    Integer outPoolNo = Integer.parseInt(zhuanyundouParts[6]);
                    MesJiaochi mesJiaochi = mesJiaochiList.stream()
                            .filter(e -> e.getMesCell().getMesArea().equals(mesArea))
                            .filter(e -> e.getJiaochiNo().equals(outPoolNo))
                            .findFirst()
                            .orElse(null);
                    mesZhuanyundou.setHuijiaoJiaochi(mesJiaochi);
                    //回窖糟：该糟醅源头—出窖时间	7
                    Date rawOutTime = mesZhuanyundou.getHuijiaoChujiaoTime();
                    Date outTime = winccDataDealCommons.convertStringToDate(zhuanyundouParts[7]);
                    mesZhuanyundou.setHuijiaoChujiaoTime(outTime);
                    //回窖糟：糟醅类型	8
                    EnumZaopeiType rawZaopeiTypeEnum = mesZhuanyundou.getHuijiaoZaopeiType() == null ? null : mesZhuanyundou.getHuijiaoZaopeiType();
                    Integer rawZaoPeiType = null;
                    if (mesZhuanyundou.getHuijiaoZaopeiType() != null) {
                        rawZaoPeiType = mesZhuanyundou.getHuijiaoZaopeiType().getId();
                    }
                    Integer zaoPeiType = Integer.parseInt(zhuanyundouParts[8]);
                    EnumZaopeiType enumZaopeiType = EnumZaopeiType.fromId(zaoPeiType);
                    mesZhuanyundou.setHuijiaoZaopeiType(enumZaopeiType);
                    //回窖糟：加曲重量	9
                    Float rawJiaquWeight = mesZhuanyundou.getHuijiaoQufeiQty() == null ? 0 : mesZhuanyundou.getHuijiaoQufeiQty();
                    Float jiaquWeight = Float.parseFloat(zhuanyundouParts[9]);
                    mesZhuanyundou.setHuijiaoQufeiQty(jiaquWeight);
                    //回窖糟：糟醅重量	10
                    Float rawZaopeiWeight = mesZhuanyundou.getHuijiaoZaopeiQty() == null ? 0 : mesZhuanyundou.getHuijiaoZaopeiQty();
                    Float zaopeiWeight = Float.parseFloat(zhuanyundouParts[10]);
                    mesZhuanyundou.setHuijiaoZaopeiQty(zaopeiWeight);
                    //回窖糟：摊晾期间出口最大温度	11
                    Float rawMaxTemperature = mesZhuanyundou.getHuijiaoChukouMaxTemp() == null ? 0 : mesZhuanyundou.getHuijiaoChukouMaxTemp();
                    Float maxTemperature = Float.parseFloat(zhuanyundouParts[11]);
                    mesZhuanyundou.setHuijiaoChukouMaxTemp(maxTemperature);
                    //回窖糟：摊晾期间出口最小温度	12
                    Float rawMinTemperature = mesZhuanyundou.getHuijiaoChukouMinTemp() == null ? 0 : mesZhuanyundou.getHuijiaoChukouMinTemp();
                    Float minTemperature = Float.parseFloat(zhuanyundouParts[12]);
                    mesZhuanyundou.setHuijiaoChukouMinTemp(minTemperature);
                    //回窖糟：摊晾开始时间	13
                    Date rawTanliangStartTime = mesZhuanyundou.getHuijiaoTanliangStartTime();
                    Date tanliangStartTime = winccDataDealCommons.convertStringToDate(zhuanyundouParts[13]);
                    mesZhuanyundou.setHuijiaoTanliangStartTime(tanliangStartTime);
                    //回窖糟：摊晾期间出口平均温度	14
                    Float rawAvgTemperature = mesZhuanyundou.getHuijiaoChukouAvgTemp() == null ? 0 : mesZhuanyundou.getHuijiaoChukouAvgTemp();
                    Float avgTemperature = Float.parseFloat(zhuanyundouParts[14]);
                    mesZhuanyundou.setHuijiaoChukouAvgTemp(avgTemperature);
                    //出窖：出层数	15
                    Integer rawOutLayerNo = mesZhuanyundou.getChujiaoJiaochiLayer() == null ? 0 : mesZhuanyundou.getChujiaoJiaochiLayer();
                    Integer outLayerNo = Integer.parseInt(zhuanyundouParts[15]);
                    mesZhuanyundou.setChujiaoJiaochiLayer(outLayerNo);
                    //出窖：窖池编号	16
                    MesJiaochi rawChujiaoJiaochi = mesZhuanyundou.getChujiaoJiaochi() == null ? null : mesZhuanyundou.getChujiaoJiaochi();
                    Integer poolNo = Integer.parseInt(zhuanyundouParts[16]);
                    MesJiaochi chujiaoJiaochi = mesJiaochiList.stream()
                            .filter(e -> e.getMesCell().getMesArea().equals(mesArea))
                            .filter(e -> e.getJiaochiNo().equals(poolNo))
                            .findFirst()
                            .orElse(null);
                    mesZhuanyundou.setChujiaoJiaochi(chujiaoJiaochi);
                    //出窖：出窖时间	17
                    Date rawChujiaoTime = mesZhuanyundou.getChujiaoTime();
                    Date chujiaoTime = winccDataDealCommons.convertStringToDate(zhuanyundouParts[17]);
                    mesZhuanyundou.setChujiaoTime(chujiaoTime);
                    //出窖：糟醅类型	18
                    EnumZaopeiType rawChujiaoZaopeiType = mesZhuanyundou.getChujiaoZaopeiType() == null ? null : mesZhuanyundou.getChujiaoZaopeiType();
                    Integer chujiaoZaopeiType = Integer.parseInt(zhuanyundouParts[18]);
                    EnumZaopeiType enumChujiaoZaopeiType = EnumZaopeiType.fromId(chujiaoZaopeiType);
                    mesZhuanyundou.setChujiaoZaopeiType(enumChujiaoZaopeiType);

                    // TODO： 是否Record 的条件可能需要调整
                    if(rawCurrentLocation == null || !rawCurrentLocation.equals(mesZhuanyundou.getCurrentLocation()) || !rawWinccZhuanyundouNo.equals(mesZhuanyundou.getWincc_No())){
                        MesZhuanyundouRecord record = dataManager.create(MesZhuanyundouRecord.class);
                        record.setMesZhuanyundou(mesZhuanyundou);
                        record.setPhaseStartId(mesZhuanyundou.getWinccUpdateId());
                        record.setPhaseStartTime(mesZhuanyundou.getWinccUpdateTime());
                        record.setPreLocation(rawCurrentLocation);

                        // 将mesRunliangdou的属性复制到 record 的属性上
                        setRecordNormalInfo(record, rawWinccZhuanyundouNo, rawCurrentLocation, rawFullOrEmpty, rawChujiaozaoOrHuijiaoZao, rawTanliangDuration, rawOutLayer, rawHuijiaoJiaochi, rawOutTime, rawZaopeiTypeEnum, rawJiaquWeight, rawZaopeiWeight, rawMaxTemperature, rawMinTemperature, rawTanliangStartTime, rawAvgTemperature, rawOutLayerNo, rawChujiaoJiaochi, rawChujiaoTime, rawChujiaoZaopeiType);
                        mesZhuanyundouRecordList.add(record);

                        /**
                         * 处理前面具体步骤的结束信息
                         */
                        MesZhuanyundouRecord preRecord = mesZhuanyundouRecordList.stream()
                                .filter(e -> e.getMesZhuanyundou().equals(mesZhuanyundou)
                                        && e.getPhaseStartTime().before(mesZhuanyundou.getWinccUpdateTime()))
                                .max(Comparator.comparing(MesZhuanyundouRecord::getPhaseStartTime))
                                .orElse(null);
                        if (preRecord != null) {
                            preRecord.setPhaseEndTime(mesZhuanyundou.getWinccUpdateTime());
                            if (preRecord.getPhaseStartTime() != null && preRecord.getPhaseEndTime() != null) {
                                long duration = preRecord.getPhaseEndTime().getTime() - preRecord.getPhaseStartTime().getTime();
                                preRecord.setPhaseDuration((float) (duration / 60000));
                            }
                            preRecord.setPhaseEndId(mesZhuanyundou.getWinccUpdateId());
                            preRecord.setPhaseEndTime(mesZhuanyundou.getWinccUpdateTime());
                            preRecord.setAfterLocation(mesZhuanyundou.getCurrentLocation());
                            setRecordNormalInfo(preRecord, rawWinccZhuanyundouNo, rawCurrentLocation, rawFullOrEmpty, rawChujiaozaoOrHuijiaoZao, rawTanliangDuration, rawOutLayer, rawHuijiaoJiaochi, rawOutTime, rawZaopeiTypeEnum, rawJiaquWeight, rawZaopeiWeight, rawMaxTemperature, rawMinTemperature, rawTanliangStartTime, rawAvgTemperature, rawOutLayerNo, rawChujiaoJiaochi, rawChujiaoTime, rawChujiaoZaopeiType);
                            mesZhuanyundouRecordList.add(preRecord);
                        }else {
                            if(rawWinccUpdateTime != null) {
                                List<MesZhuanyundouRecord> preRecords = dataManager.load(MesZhuanyundouRecord.class)
                                        .query("select e from MesZhuanyundouRecord e " +
                                                "where e.mesZhuanyundou = :mesZhuanyundou " +
                                                "and e.phaseStartTime <= :phaseStartTime " +
                                                "order by e.phaseStartTime desc")
                                        .parameter("mesZhuanyundou", mesZhuanyundou)
                                        .parameter("phaseStartTime", rawWinccUpdateTime)
                                        .maxResults(1)
                                        .list();
                                if (!preRecords.isEmpty()) {
                                    MesZhuanyundouRecord preRecord1 = preRecords.get(0);
                                    preRecord1.setPhaseEndTime(mesZhuanyundou.getWinccUpdateTime());
                                    if (preRecord1.getPhaseStartTime() != null && preRecord1.getPhaseEndTime() != null) {
                                        long duration = preRecord1.getPhaseEndTime().getTime() - preRecord1.getPhaseStartTime().getTime();
                                        preRecord1.setPhaseDuration((float) (duration / 60000));
                                    }
                                    preRecord1.setPhaseEndId(mesZhuanyundou.getWinccUpdateId());
                                    preRecord1.setPhaseEndTime(mesZhuanyundou.getWinccUpdateTime());
                                    preRecord1.setAfterLocation(mesZhuanyundou.getCurrentLocation());
                                    setRecordNormalInfo(preRecord1, rawWinccZhuanyundouNo, rawCurrentLocation, rawFullOrEmpty, rawChujiaozaoOrHuijiaoZao, rawTanliangDuration, rawOutLayer, rawHuijiaoJiaochi, rawOutTime, rawZaopeiTypeEnum, rawJiaquWeight, rawZaopeiWeight, rawMaxTemperature, rawMinTemperature, rawTanliangStartTime, rawAvgTemperature, rawOutLayerNo, rawChujiaoJiaochi, rawChujiaoTime, rawChujiaoZaopeiType);
                                    mesZhuanyundouRecordList.add(preRecord1);
                                }
                            }
                        }
                    }
                }
            }
            jobSaveDataService.saveZhuanyundouRecordData(jobConfig, areaZhuanyundouList, mesZhuanyundouRecordList, maxWinccId);
            mesZhuanyundouRecordList.clear();
        }

        }


    private static void setRecordNormalInfo(MesZhuanyundouRecord record, Integer rawWinccZhuanyundouNo, MesWinccItemConfig rawCurrentLocation, MesWinccItemConfig rawFullOrEmpty, MesWinccItemConfig rawChujiaozaoOrHuijiaoZao, Float rawTanliangDuration, Integer rawOutLayer, MesJiaochi rawHuijiaoJiaochi, Date rawOutTime, EnumZaopeiType rawZaopeiTypeEnum, Float rawJiaquWeight, Float rawZaopeiWeight, Float rawMaxTemperature, Float rawMinTemperature, Date rawTanliangStartTime, Float rawAvgTemperature, Integer rawOutLayerNo, MesJiaochi rawChujiaoJiaochi, Date rawChujiaoTime, EnumZaopeiType rawChujiaoZaopeiType) {
        record.setWincc_No(rawWinccZhuanyundouNo);
        record.setCurrentLocation(rawCurrentLocation);
        record.setFullOrEmpty(rawFullOrEmpty);
        record.setChujiaozaoOrHuijiaoZao(rawChujiaozaoOrHuijiaoZao);
        record.setHuijiaoTanliangDuration(rawTanliangDuration);
        record.setHuijiaoJiaochiLayer(rawOutLayer);
        record.setHuijiaoJiaochi(rawHuijiaoJiaochi);
        record.setHuijiaoChujiaoTime(rawOutTime);
        record.setHuijiaoZaopeiType(rawZaopeiTypeEnum);
        record.setHuijiaoQufeiQty(rawJiaquWeight);
        record.setHuijiaoZaopeiQty(rawZaopeiWeight);
        record.setHuijiaoChukouMaxTemp(rawMaxTemperature);
        record.setHuijiaoChukouMinTemp(rawMinTemperature);
        record.setHuijiaoTanliangStartTime(rawTanliangStartTime);
        record.setHuijiaoChukouAvgTemp(rawAvgTemperature);
        record.setChujiaoJiaochiLayer(rawOutLayerNo);
        record.setChujiaoJiaochi(rawChujiaoJiaochi);
        record.setChujiaoTime(rawChujiaoTime);
        record.setChujiaoZaopeiType(rawChujiaoZaopeiType);
    }
}
