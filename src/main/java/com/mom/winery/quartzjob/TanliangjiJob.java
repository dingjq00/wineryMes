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
 * @Date 2024/10/27 15:56
 */
@DisallowConcurrentExecution
public class TanliangjiJob implements Job {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    @Authenticated
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<JobConfig> jobConfigList = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mainPhase = :mainPhase")
                .parameter("mainPhase", EnumProcessMainPhase.TANLIANGJI)
                .list();
        List<MesTanliangji> mesTanliangjiList = dataManager.load(MesTanliangji.class)
                .query("select e from MesTanliangji e")
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


        for (JobConfig jobConfig : jobConfigList) {
            List<MesTanliangjiRecord> mesTanliangjiRecordList = new ArrayList<>();

            MesArea mesArea = jobConfig.getMesArea();
            Integer areaCode = mesArea.getAreaCode();
            Integer currentWinccId = jobConfig.getWinccId();
            /**
             * 获取每个单元的摊晾机
             */
            List<MesTanliangji> areaTanliangjiList = mesTanliangjiList.stream()
                    .filter(mesTanliangji -> mesTanliangji.getMesArea().equals(mesArea))
                    .sorted(Comparator.comparing(MesTanliangji::getTanliangjiCode))
                    .toList();
            int areaTanliangjiListSize = areaTanliangjiList.size();
            if (areaTanliangjiListSize == 0) {
                continue;
            }

            /**
             * 解决如果通过 Nifi获取 Wincc 数据出错时的处理
             */
            Integer currentMesWinccId = areaTanliangjiList.stream()
                    .map(mesTanliangji -> Optional.ofNullable(mesTanliangji.getWinccStartId()).orElse(0))
                    .max(Integer::compareTo)
                    .orElse(0);
            if (currentMesWinccId > currentWinccId){
                List<WinccTanliangji> preWinccTanliangjiList = dataManager.load(WinccTanliangji.class)
                        .query("select e from WinccTanliangji e " +
                                "where e.areaNo = :areaCode and e.winccId <= :currentWinccId " +
                                "order by e.winccId desc")
                        .parameter("areaCode", areaCode)
                        .parameter("currentWinccId", currentWinccId)
                        .maxResults(1)
                        .list();
                if (!preWinccTanliangjiList.isEmpty()) {
                    WinccTanliangji preWinccTanliangji = preWinccTanliangjiList.getFirst();
                    // 将这个preWinccTanliangji的数据更新到mesTanliangji的数据上
                    resetTanliangjiInMemory(preWinccTanliangji, areaTanliangjiListSize, areaTanliangjiList, mesWinccItemConfigList, mesJiaochiList, mesArea, mesZengguoList);
                    List<MesTanliangjiRecord> preMesTanliangjiRecordList = dataManager.load(MesTanliangjiRecord.class)
                            .query("select e from MesTanliangjiRecord e " +
                                    "where e.mesTanliangji.mesArea = :mesArea " +
                                    "and e.winccStartId > :preWinccId ")
                            .parameter("mesArea", mesArea)
                            .parameter("preWinccId", preWinccTanliangji.getWinccId())
                            .list();
                    dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).removing(preMesTanliangjiRecordList));
                } else {
                    clearTanliangjiProperties(areaTanliangjiList, mesArea);
                }
            }




            /**
             * 获取每个单元Wincc 摊晾机待处理的数据
             */

            List<WinccTanliangji> winccTanliangjiList = dataManager.load(WinccTanliangji.class)
                    .query("select e from WinccTanliangji e " +
                            "where e.areaNo = :areaCode and e.winccId > :currentWinccId " +
                            "order by e.winccId")
                    .parameter("areaCode", areaCode)
                    .parameter("currentWinccId", currentWinccId)
                    .list();
            if (winccTanliangjiList.isEmpty()) {
                continue;
            }
            // 获取最大的winccId, 用于更新JobConfig的winccId
            Integer maxWinccId = winccTanliangjiList.stream()
                    .map(WinccTanliangji::getWinccId)
                    .max(Integer::compareTo)
                    .orElse(0);

            /**
             * 处理Wincc 摊晾机待处理的数据
             */

            for (WinccTanliangji winccTanliangji : winccTanliangjiList) {
                Date winccUpdateTime = winccTanliangji.getStarttime();
                Integer winccId = winccTanliangji.getWinccId();
                String comment = winccTanliangji.getComment();
                // 如果comment 为null 或为空，不处理
                if (comment == null || comment.isEmpty()) {
                    continue;
                }
                // Split the comment string by commas
                String[] commentParts = comment.split(",");
                int propertyNumber = commentParts.length / areaTanliangjiListSize;

                for (int i = 0; i < areaTanliangjiListSize; i++) {
                    MesTanliangji mesTanliangji = areaTanliangjiList.get(i);
                    String tanliangjiCode = mesTanliangji.getTanliangjiCode();
                    int startIndex = i * propertyNumber;
                    int endIndex = (i + 1) * propertyNumber;
                    String[] tanliangjiParts = Arrays.copyOfRange(commentParts, startIndex, endIndex);

                    // wincc更新Id
                    Integer rawWinccId = mesTanliangji.getWinccStartId()==null ? 0 : mesTanliangji.getWinccStartId();
                    mesTanliangji.setWinccStartId(winccId);

                    // wincc更新时间
                    Date rawWinccUpdateTime = mesTanliangji.getWinccStartTime();
                    mesTanliangji.setWinccUpdateTime(winccUpdateTime);


                    // 获取增锅
                    MesZengguo rawZengguo = mesTanliangji.getResourceZengguo();
                    int zengguoNo = Integer.parseInt(tanliangjiParts[0]);
                    MesZengguo zengguo = mesZengguoList.stream()
                            .filter(e -> e.getZengguoCode().equals(zengguoNo))
                            .findFirst()
                            .orElse(null);
                    mesTanliangji.setResourceZengguo(zengguo);

                    // 获取打水量
                    float rawDashuiliang = mesTanliangji.getLiangshuiAddQty() == null ? 0 : mesTanliangji.getLiangshuiAddQty();
                    float dashuiliang = Float.parseFloat(tanliangjiParts[1]);
                    mesTanliangji.setLiangshuiAddQty(dashuiliang);

                    // 获取甑序
                    int rawZengxu = mesTanliangji.getZengSequence() == null ? -1 : mesTanliangji.getZengSequence();
                    int zengxu = Integer.parseInt(tanliangjiParts[2]);
                    mesTanliangji.setZengSequence(zengxu);

                    // 下半甑加稻壳量
                    float rawXiaBanJiaDaoKeLiang = mesTanliangji.getDaokeAddQtyDown() == null ? 0 : mesTanliangji.getDaokeAddQtyDown();
                    float xiaBanJiaDaoKeLiang = Float.parseFloat(tanliangjiParts[3]);
                    mesTanliangji.setDaokeAddQtyDown(xiaBanJiaDaoKeLiang);

                    // 下半甑加粮食量
                    float rawXiaBanJiaLiangShiLiang = mesTanliangji.getLiangshiAddQtyDown() == null ? 0 : mesTanliangji.getLiangshiAddQtyDown();
                    float xiaBanJiaLiangShiLiang = Float.parseFloat(tanliangjiParts[4]);
                    mesTanliangji.setLiangshiAddQtyDown(xiaBanJiaLiangShiLiang);

                    // 下半甑加糟醅量
                    float rawXiaBanJiaZaoPeiLiang = mesTanliangji.getZaopeiAddQtyDown() == null ? 0 : mesTanliangji.getZaopeiAddQtyDown();
                    float xiaBanJiaZaoPeiLiang = Float.parseFloat(tanliangjiParts[5]);
                    mesTanliangji.setZaopeiAddQtyDown(xiaBanJiaZaoPeiLiang);

                    // 上半甑加稻壳量
                    float rawShangBanJiaDaoKeLiang = mesTanliangji.getDaokeAddQtyUp() == null ? 0 : mesTanliangji.getDaokeAddQtyUp();
                    float shangBanJiaDaoKeLiang = Float.parseFloat(tanliangjiParts[6]);
                    mesTanliangji.setDaokeAddQtyUp(shangBanJiaDaoKeLiang);

                    // 上半甑加粮食量
                    float rawShangBanJiaLiangShiLiang = mesTanliangji.getLiangshiAddQtyUp() == null ? 0 : mesTanliangji.getLiangshiAddQtyUp();
                    float shangBanJiaLiangShiLiang = Float.parseFloat(tanliangjiParts[7]);
                    mesTanliangji.setLiangshiAddQtyUp(shangBanJiaLiangShiLiang);

                    // 上半甑加糟醅量
                    float rawShangBanJiaZaoPeiLiang = mesTanliangji.getZaopeiAddQtyUp() == null ? 0 : mesTanliangji.getZaopeiAddQtyUp();
                    float shangBanJiaZaoPeiLiang = Float.parseFloat(tanliangjiParts[8]);
                    mesTanliangji.setZaopeiAddQtyUp(shangBanJiaZaoPeiLiang);

                    // 蒸馏时长
                    float rawZhengliuShiChang = mesTanliangji.getZhengliuDuration() == null ? 0 : mesTanliangji.getZhengliuDuration();
                    float zhengliuShiChang = Float.parseFloat(tanliangjiParts[9]);
                    mesTanliangji.setZhengliuDuration(zhengliuShiChang);

                    // 上甑时长
                    float rawShangZengShiChang = mesTanliangji.getShangzengDuration() == null ? 0 : mesTanliangji.getShangzengDuration();
                    float shangZengShiChang = Float.parseFloat(tanliangjiParts[10]);
                    mesTanliangji.setShangzengDuration(shangZengShiChang);

                    // 糟醅类型
                    EnumZaopeiType rawZaoPeiType = mesTanliangji.getZaopeiType() == null ? EnumZaopeiType.UNKNOW : mesTanliangji.getZaopeiType();
                    int zaopeiType = Integer.parseInt(tanliangjiParts[11]);
                    EnumZaopeiType zaoPeiType = EnumZaopeiType.fromId(zaopeiType);
                    mesTanliangji.setZaopeiType(zaoPeiType);

                    // 上甑累计重量
                    float rawShangzengTotalQty = mesTanliangji.getShangzengTotalQty() == null ? 0 : mesTanliangji.getShangzengTotalQty();
                    float shangzengTotalQty = Float.parseFloat(tanliangjiParts[12]);
                    mesTanliangji.setShangzengTotalQty(shangzengTotalQty);

                    MesZhuanyundou rawZhuanyundou = mesTanliangji.getCurrentZhuanyundou();
                    int zhuanyundouNo = Integer.parseInt(tanliangjiParts[13]);
                    MesZhuanyundou zhuanyundouNew = dataManager.load(MesZhuanyundou.class)
                            .query("select e from MesZhuanyundou e where e.zhuanyundouNo = :zhuanyundouNo")
                            .parameter("zhuanyundouNo", zhuanyundouNo)
                            .optional().orElse(null);
                    mesTanliangji.setCurrentZhuanyundou(zhuanyundouNew);

                    // TODO： 是否Record 的条件可能需要调整
                    // 注意：winccStartTime 代表着摊晾开始时间及摊晾结束时间，是 wincc计算的数值
                    if(rawZengxu == -1 || rawZengxu != (mesTanliangji.getZengSequence())) {
                        MesTanliangjiRecord record = dataManager.create(MesTanliangjiRecord.class);
                        record.setMesTanliangji(mesTanliangji);
                        record.setPhaseStartTime(mesTanliangji.getWinccUpdateTime());
                        record.setWinccStartId(mesTanliangji.getWinccStartId());
                        record.setWinccStartTime(mesTanliangji.getWinccStartTime());
                        record.setWinccEndTime(mesTanliangji.getWinccEndTime());
                        record.setResourceZengguo(mesTanliangji.getResourceZengguo());
                        record.setLiangshuiAddQty(mesTanliangji.getLiangshuiAddQty());
                        record.setZengSequence(mesTanliangji.getZengSequence());
                        record.setDaokeAddQtyDown(mesTanliangji.getDaokeAddQtyDown());
                        record.setLiangshiAddQtyDown(mesTanliangji.getLiangshiAddQtyDown());
                        record.setZaopeiAddQtyDown(mesTanliangji.getZaopeiAddQtyDown());
                        record.setDaokeAddQtyUp(mesTanliangji.getDaokeAddQtyUp());
                        record.setLiangshiAddQtyUp(mesTanliangji.getLiangshiAddQtyUp());
                        record.setZaopeiAddQtyUp(mesTanliangji.getZaopeiAddQtyUp());
                        record.setZhengliuDuration(mesTanliangji.getZhengliuDuration());
                        record.setShangzengDuration(mesTanliangji.getShangzengDuration());
                        record.setZaopeiType(mesTanliangji.getZaopeiType());
                        record.setShangzengTotalQty(mesTanliangji.getShangzengTotalQty());

                        mesTanliangjiRecordList.add(record);

                        MesTanliangjiRecord preRecord = mesTanliangjiRecordList.stream()
                                .filter(e -> e.getMesTanliangji().equals(mesTanliangji)
                                        && e.getPhaseStartTime().before(mesTanliangji.getWinccUpdateTime()))
                                .max(Comparator.comparing(MesTanliangjiRecord::getPhaseStartTime))
                                .orElse(null);
                        if (preRecord != null) {
                            preRecord.setPhaseEndTime(mesTanliangji.getWinccUpdateTime());
                            if (preRecord.getPhaseStartTime() != null && preRecord.getPhaseEndTime() != null) {
                                long duration = preRecord.getPhaseEndTime().getTime() - preRecord.getPhaseStartTime().getTime();
                                preRecord.setPhaseDuration((float) (duration / 60000));
                            }
                            preRecord.setWinccEndId(mesTanliangji.getWinccStartId());

                            setPrerecordNormalInfo(preRecord, rawZengguo, rawDashuiliang, rawZengxu, rawXiaBanJiaDaoKeLiang, rawXiaBanJiaLiangShiLiang, rawXiaBanJiaZaoPeiLiang, rawShangBanJiaDaoKeLiang, rawShangBanJiaLiangShiLiang, rawShangBanJiaZaoPeiLiang, rawZhengliuShiChang, rawShangZengShiChang, rawZaoPeiType, rawShangzengTotalQty, rawZhuanyundou);

                            mesTanliangjiRecordList.add(preRecord);
                        } else {
                            if (rawWinccUpdateTime != null) {
                                List<MesTanliangjiRecord> preRecords = dataManager.load(MesTanliangjiRecord.class)
                                        .query("select e from MesTanliangjiRecord e " +
                                                "where e.mesTanliangji = :mesTanliangji " +
                                                "and e.winccStartTime <= :winccStartTime " +
                                                "order by e.winccStartTime desc")
                                        .parameter("mesTanliangji", mesTanliangji)
                                        .parameter("winccStartTime", rawWinccUpdateTime)
                                        .maxResults(1)
                                        .list();
                                if (!preRecords.isEmpty()) {
                                    MesTanliangjiRecord preRecord1 = preRecords.get(0);
                                    preRecord1.setPhaseEndTime(mesTanliangji.getWinccUpdateTime());
                                    if (preRecord1.getPhaseEndTime() != null && preRecord1.getPhaseEndTime() != null) {
                                        long duration = preRecord1.getPhaseEndTime().getTime() - preRecord1.getPhaseEndTime().getTime();
                                        preRecord1.setPhaseDuration((float) (duration / 60000));
                                    }
                                    preRecord1.setWinccEndId(mesTanliangji.getWinccStartId());
                                    setPrerecordNormalInfo(preRecord1, rawZengguo, rawDashuiliang, rawZengxu, rawXiaBanJiaDaoKeLiang, rawXiaBanJiaLiangShiLiang, rawXiaBanJiaZaoPeiLiang, rawShangBanJiaDaoKeLiang, rawShangBanJiaLiangShiLiang, rawShangBanJiaZaoPeiLiang, rawZhengliuShiChang, rawShangZengShiChang, rawZaoPeiType, rawShangzengTotalQty, rawZhuanyundou);
                                    mesTanliangjiRecordList.add(preRecord1);
                                }
                            }
                        }
                    }
                }
            }
            saveData(areaTanliangjiList, mesTanliangjiRecordList, jobConfig, maxWinccId);
            mesTanliangjiRecordList.clear();
        }
    }

    private static void setPrerecordNormalInfo(MesTanliangjiRecord preRecord, MesZengguo rawZengguo, float rawDashuiliang, int rawZengxu, float rawXiaBanJiaDaoKeLiang, float rawXiaBanJiaLiangShiLiang, float rawXiaBanJiaZaoPeiLiang, float rawShangBanJiaDaoKeLiang, float rawShangBanJiaLiangShiLiang, float rawShangBanJiaZaoPeiLiang, float rawZhengliuShiChang, float rawShangZengShiChang, EnumZaopeiType rawZaoPeiType, float rawShangzengTotalQty, MesZhuanyundou rawZhuanyundou) {
        preRecord.setResourceZengguo(rawZengguo);
        preRecord.setLiangshuiAddQty(rawDashuiliang);
        preRecord.setZengSequence(rawZengxu);
        preRecord.setDaokeAddQtyDown(rawXiaBanJiaDaoKeLiang);
        preRecord.setLiangshiAddQtyDown(rawXiaBanJiaLiangShiLiang);
        preRecord.setZaopeiAddQtyDown(rawXiaBanJiaZaoPeiLiang);
        preRecord.setDaokeAddQtyUp(rawShangBanJiaDaoKeLiang);
        preRecord.setLiangshiAddQtyUp(rawShangBanJiaLiangShiLiang);
        preRecord.setZaopeiAddQtyUp(rawShangBanJiaZaoPeiLiang);
        preRecord.setZhengliuDuration(rawZhengliuShiChang);
        preRecord.setShangzengDuration(rawShangZengShiChang);
        preRecord.setZaopeiType(rawZaoPeiType);
        preRecord.setShangzengTotalQty(rawShangzengTotalQty);
        preRecord.setCurrentZhuanyundou(rawZhuanyundou);
    }

    private void clearTanliangjiProperties(List<MesTanliangji> areaTanliangjiList, MesArea mesArea) {
        List<MesTanliangjiRecord> preMesTanliangjiRecordList = dataManager.load(MesTanliangjiRecord.class)
                .query("select e from MesTanliangjiRecord e where e.mesTanliangji.mesArea = :mesArea ")
                .parameter("mesArea", mesArea)
                .list();
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).removing(preMesTanliangjiRecordList));
        for (MesTanliangji mesTanliangji : areaTanliangjiList) {
            mesTanliangji.setWinccStartId(null);
            mesTanliangji.setWinccStartTime(null);
            mesTanliangji.setResourceZengguo(null);
            mesTanliangji.setLiangshuiAddQty(null);
            mesTanliangji.setZengSequence(null);
            mesTanliangji.setDaokeAddQtyDown(null);
            mesTanliangji.setLiangshiAddQtyDown(null);
            mesTanliangji.setZaopeiAddQtyDown(null);
            mesTanliangji.setDaokeAddQtyUp(null);
            mesTanliangji.setLiangshiAddQtyUp(null);
            mesTanliangji.setZaopeiAddQtyUp(null);
            mesTanliangji.setZhengliuDuration(null);
            mesTanliangji.setShangzengDuration(null);
            mesTanliangji.setZaopeiType(null);
            mesTanliangji.setShangzengTotalQty(null);
            mesTanliangji.setCurrentZhuanyundou(null);
            mesTanliangji.setWinccUpdateTime(null);
        }
    }

    private void resetTanliangjiInMemory(WinccTanliangji preWinccTanliangji, int areaTanliangjiListSize, List<MesTanliangji> areaTanliangjiList, List<MesWinccItemConfig> mesWinccItemConfigList, List<MesJiaochi> mesJiaochiList, MesArea mesArea, List<MesZengguo> mesZengguoList) {
        String comment = preWinccTanliangji.getComment();
        String[] commentParts = comment.split(",");
        int propertyNumber = commentParts.length / areaTanliangjiListSize;
        for (int i = 0; i < areaTanliangjiListSize; i++) {
            MesTanliangji mesTanliangji = areaTanliangjiList.get(i);
            String tanliangjiCode = mesTanliangji.getTanliangjiCode();
            int startIndex = i * propertyNumber;
            int endIndex = (i + 1) * propertyNumber;
            String[] tanliangjiParts = Arrays.copyOfRange(commentParts, startIndex, endIndex);

            // 更新WinccId以及WinccUpdateTime
            mesTanliangji.setWinccStartId(preWinccTanliangji.getWinccId());
            mesTanliangji.setWinccUpdateTime(preWinccTanliangji.getStarttime());

            // 获取增锅
            MesZengguo mesZengguo = mesTanliangji.getResourceZengguo();
            int zengguoNo = Integer.parseInt(tanliangjiParts[0]);
            MesZengguo zengguo = mesZengguoList.stream()
                    .filter(e -> e.getZengguoCode().equals(zengguoNo))
                    .findFirst()
                    .orElse(null);
            mesTanliangji.setResourceZengguo(zengguo);

            // 获取打水量
            float dashuiliang = Float.parseFloat(tanliangjiParts[1]);
            mesTanliangji.setLiangshuiAddQty(dashuiliang);

            // 获取甑序
            int zengxu = Integer.parseInt(tanliangjiParts[2]);
            mesTanliangji.setZengSequence(zengxu);

            // 下半甑加稻壳量
            float xiaBanJiaDaoKeLiang = Float.parseFloat(tanliangjiParts[3]);
            mesTanliangji.setDaokeAddQtyDown(xiaBanJiaDaoKeLiang);

            // 下半甑加粮食量
            float xiaBanJiaLiangShiLiang = Float.parseFloat(tanliangjiParts[4]);
            mesTanliangji.setLiangshiAddQtyDown(xiaBanJiaLiangShiLiang);

            // 下半甑加糟醅量
            float xiaBanJiaZaoPeiLiang = Float.parseFloat(tanliangjiParts[5]);
            mesTanliangji.setZaopeiAddQtyDown(xiaBanJiaZaoPeiLiang);

            // 上半甑加稻壳量
            float shangBanJiaDaoKeLiang = Float.parseFloat(tanliangjiParts[6]);
            mesTanliangji.setDaokeAddQtyUp(shangBanJiaDaoKeLiang);

            // 上半甑加粮食量
            float shangBanJiaLiangShiLiang = Float.parseFloat(tanliangjiParts[7]);
            mesTanliangji.setLiangshiAddQtyUp(shangBanJiaLiangShiLiang);

            // 上半甑加糟醅量
            float shangBanJiaZaoPeiLiang = Float.parseFloat(tanliangjiParts[8]);
            mesTanliangji.setZaopeiAddQtyUp(shangBanJiaZaoPeiLiang);

            // 蒸馏时长
            float zhengliuShiChang = Float.parseFloat(tanliangjiParts[9]);
            mesTanliangji.setZhengliuDuration(zhengliuShiChang);

            // 上甑时长
            float shangZengShiChang = Float.parseFloat(tanliangjiParts[10]);
            mesTanliangji.setShangzengDuration(shangZengShiChang);

            // 糟醅类型
            int zaopeiType = Integer.parseInt(tanliangjiParts[11]);
            EnumZaopeiType zaoPeiType = EnumZaopeiType.fromId(zaopeiType);
            mesTanliangji.setZaopeiType(zaoPeiType);

            // 上甑累计重量
            float shangzengTotalQty = Float.parseFloat(tanliangjiParts[12]);
            mesTanliangji.setShangzengTotalQty(shangzengTotalQty);

            // TODO: 该摊晾机正在给?#转运斗喂料, 暂时先不处理，wincc 数据有问题。
            mesTanliangji.setCurrentZhuanyundou(null);
        }

    }

    @Transactional
    public void saveData(List<MesTanliangji> areaTanliangjiList , List<MesTanliangjiRecord> mesTanliangjiRecordList, JobConfig jobConfig, Integer maxWinccId) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(areaTanliangjiList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesTanliangjiRecordList));
        jobConfig.setWinccId(maxWinccId);
        dataManager.save(new SaveContext().setDiscardSaved(true).saving(jobConfig));
    }
}
