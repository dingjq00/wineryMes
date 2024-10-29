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

                        // 设置 Record
                        MesZengguoRecord preRecord = mesZengguoRecordList.stream()
                                .filter(e -> e.getMesZengguo().equals(mesZengguo)
                                        && e.getPhaseStartTimeTotal().before(mesZengguo.getWinccUpdateTime()))
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
            saveData(jobConfig, areaZengguoList, mesZengguoRecordList, maxWinccId);
        }
    }
    @Transactional
    public void saveData(JobConfig jobConfig, List<MesZengguo> areaZengguoList, List<MesZengguoRecord> mesZengguoRecordList, Integer maxWinccId) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(areaZengguoList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesZengguoRecordList));
        jobConfig.setWinccId(maxWinccId);
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(jobConfig));
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
}
