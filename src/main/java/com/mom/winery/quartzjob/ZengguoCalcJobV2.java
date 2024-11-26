package com.mom.winery.quartzjob;

import com.mom.winery.app.WinccDataDealCommons;
import com.mom.winery.entity.MesZenggouPhaseConfig;
import com.mom.winery.entity.MesZengguoRealDataV2;
import com.mom.winery.entity.MesZengguoRecord;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.core.security.Authenticated;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/14 12:18
 */
@DisallowConcurrentExecution
public class ZengguoCalcJobV2 implements Job {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;
    @Autowired
    private JobSaveDataService jobSaveDataService;

    @Authenticated
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date minDate = winccDataDealCommons.convertStringToDate("2021/01/01 00:00:00");
        List<MesZengguoRecord> zhengzhuRecordList = dataManager.load(MesZengguoRecord.class)
            .query("select e from MesZengguoRecord e " +
                    "where (e.shangzengXiaolv is null or e.shangzengXiaolv <= :minXiaolv) " +
                    "and e.zengguoPhase.phaseNo = :zengguoPhaseNo  " +
                    "and e.phaseEndTimeTotal is not null " +
                    "and e.endTimeTall > :minDate " +
                    "and (e.jiejiuDurationFirstClass > 0.2 or e.jiejiuDurationSecondClass > 0.2 " +
                    "or e.jiejiuDurationThirdClass > 0.2 or e.jiejiuDurationFeishui > 0.2)")
            .parameter("minXiaolv", 0.0)
                .parameter("zengguoPhaseNo",525)
                .parameter("minDate", minDate)
                .maxResults(200)
            .list();

        for (MesZengguoRecord zhengzhuRecord : zhengzhuRecordList) {
            // 卡盘溜酒开始时间
            Date kapanLiujiuStartTime = zhengzhuRecord.getStartTimeKagai();
            // 溜酒结束时间
            Date liujiuEndTime = zhengzhuRecord.getEndTimeLiujiu();
            // 计算开始结束总时长
            float liujiuTotalDuration = (float) ((liujiuEndTime.getTime() - kapanLiujiuStartTime.getTime()) / 1000) / 60;
            // 酒头溜酒时长(分钟为单位)
            float jiutouDuration = liujiuTotalDuration - (zhengzhuRecord.getJiejiuDurationFirstClass() + zhengzhuRecord.getJiejiuDurationSecondClass()
                    + zhengzhuRecord.getJiejiuDurationThirdClass() + zhengzhuRecord.getJiejiuDurationFeishui()) ;
            // 一级酒开始时间 = 卡盘溜酒开始时间 +  酒头溜酒时长
            Date firstClassStartTime = new Date(kapanLiujiuStartTime.getTime() + (long) (jiutouDuration * 60 * 1000));
            Date secondClassStartTime = new Date(firstClassStartTime.getTime() + (long) (zhengzhuRecord.getJiejiuDurationFirstClass() * 60 * 1000));
            Date thirdClassStartTime = new Date(secondClassStartTime.getTime() + (long) (zhengzhuRecord.getJiejiuDurationSecondClass() * 60 * 1000));
            Date jiuweiStartTime = new Date(thirdClassStartTime.getTime() + (long) (zhengzhuRecord.getJiejiuDurationThirdClass() * 60 * 1000));

            List<MesZengguoRealDataV2> zengguoRealDataList = dataManager.load(MesZengguoRealDataV2.class)
                    .query("select e from MesZengguoRealDataV2 e where e.mesZengguo = :mesZengguo " +
                            "and e.winccUpdateTime >= :kapanLiujiuStartTime " +
                            "and e.winccUpdateTime <= :liujiuEndTime")
                    .parameter("mesZengguo", zhengzhuRecord.getMesZengguo())
                    .parameter("kapanLiujiuStartTime", kapanLiujiuStartTime)
                    .parameter("liujiuEndTime", liujiuEndTime)
                    .list();
            if (zengguoRealDataList.isEmpty()){
                continue;
            }

            List<MesZengguoRealDataV2> thirdClassRealDataList = null;
            float thirdClassMaxTemp;
            thirdClassRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(thirdClassStartTime) >= 0
                                && e.getWinccUpdateTime().compareTo(jiuweiStartTime) < 0)
                        .toList();
            thirdClassMaxTemp = thirdClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);
            float thirdClassMinTemp = thirdClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .min(Float::compareTo)
                        .orElse(0.0f);

            List<MesZengguoRealDataV2> jiuweiRealDataList = null;
            float jiuweiMaxTemp;
            jiuweiRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(jiuweiStartTime) >= 0
                                && e.getWinccUpdateTime().compareTo(liujiuEndTime) <= 0)
                        .toList();
            jiuweiMaxTemp = jiuweiRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);


            List<MesZengguoRealDataV2> jiutouRealDataList = null;

            jiutouRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(kapanLiujiuStartTime) >= 0
                                && e.getWinccUpdateTime().compareTo(firstClassStartTime) < 0)
                        .toList();

            List<MesZengguoRealDataV2> firstClassRealDataList = null;
            float firstClassMaxTemp = 0.0f;
            float firstClassMinTemp = 0.0f;
            firstClassRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(firstClassStartTime)>= 0
                                && e.getWinccUpdateTime().compareTo(secondClassStartTime) < 0)
                        .toList();
            firstClassMaxTemp = firstClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);
            firstClassMinTemp = firstClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .min(Float::compareTo)
                        .orElse(0.0f);


            List<MesZengguoRealDataV2> secondClassRealDataList = null;
            float secondClassMaxTemp = 0.0f;
            float secondClassMinTemp = 0.0f;
            secondClassRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(secondClassStartTime) >= 0
                                && e.getWinccUpdateTime().compareTo(thirdClassStartTime) < 0)
                        .toList();
            secondClassMaxTemp = secondClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);
            secondClassMinTemp = secondClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .min(Float::compareTo)
                        .orElse(0.0f);

            if(thirdClassMaxTemp > 1 && jiuweiMaxTemp > 1){
                //一级酒面积
                Float firstClassArea = 0.0f;
                List<Float> firstClassAreaList = null;
                if (!firstClassRealDataList.isEmpty()) {
                    firstClassAreaList = firstClassRealDataList.stream()
                            .map(MesZengguoRealDataV2::getGuoniWendu)
                            .toList();
                    // 用三级酒最大温度减去每一个的值，如果值<0,则为0,然后求和
                    firstClassArea += firstClassAreaList.stream()
                            .map(e -> thirdClassMaxTemp - e)
                            .map(e -> e < 0 ? 0 : e)
                            .reduce(Float::sum)
                            .orElse(0.0f);
                }
                // 二级酒面积
                Float secondClassArea = 0.0f;
                List<Float> secondClassAreaList = null;
                if (!secondClassRealDataList.isEmpty()) {
                    secondClassAreaList = secondClassRealDataList.stream()
                            .map(MesZengguoRealDataV2::getGuoniWendu)
                            .toList();
                    // 用三级酒最大温度减去每一个的值，如果值<0,则为0,然后求和
                    secondClassArea += secondClassAreaList.stream()
                            .map(e -> thirdClassMaxTemp - e)
                            .map(e -> e < 0 ? 0 : e)
                            .reduce(Float::sum)
                            .orElse(0.0f);
                }
                // 三级酒面积
                float thirdClassArea = 0.0f;
                List<Float> thirdClassAreaList = null;
                if (!thirdClassRealDataList.isEmpty()) {
                    thirdClassAreaList = thirdClassRealDataList.stream()
                            .map(MesZengguoRealDataV2::getGuoniWendu)
                            .toList();
                    // 用三级酒最大温度减去每一个的值，如果值<0,则为0,然后求和
                    thirdClassArea += thirdClassAreaList.stream()
                            .map(e -> thirdClassMaxTemp - e)
                            .map(e -> e < 0 ? 0 : e)
                            .reduce(Float::sum)
                            .orElse(0.0f);
                }

                // 酒尾酒面积
                float jiuweiAreaFull = 0.0f;
                List<Float> jiuweiAreaList = null;
                if (!jiuweiRealDataList.isEmpty()) {
                    jiuweiAreaList = jiuweiRealDataList.stream()
                            .map(MesZengguoRealDataV2::getGuoniWendu)
                            .filter(guoniWendu -> guoniWendu <= 100 )
                            .toList();
                    float maxJiuweiTemp = jiuweiAreaList.stream()
                            .max(Float::compareTo)
                            .orElse(0.0f);

                    // 用酒尾最大温度减去每一个的值，如果值<0,则为0,然后求和
                    jiuweiAreaFull = jiuweiAreaList.stream()
                            .map(e -> maxJiuweiTemp - e)
                            .map(e -> e < 0 ? 0 : e)
                            .reduce(Float::sum)
                            .orElse(0.0f);
                }

                // 计算装甑效率
                float zhuangzengXiaolv = (firstClassArea + secondClassArea + thirdClassArea) / (firstClassArea + secondClassArea + thirdClassArea+ jiuweiAreaFull);
                zhuangzengXiaolv = zhuangzengXiaolv * 100;
                zhengzhuRecord.setShangzengXiaolv(zhuangzengXiaolv);
            }

            // 计算一级二级酒斜率
            float minFirstSecondClass = 0.0f;
            float maxFirstSecondClass = 0.0f;
            if(firstClassMinTemp > 1 ){
                minFirstSecondClass = firstClassMinTemp;
            }else if(secondClassMinTemp > 1){
                minFirstSecondClass = secondClassMinTemp;
            }
            if(secondClassMaxTemp > 1) {
                maxFirstSecondClass = secondClassMaxTemp;
            } else if (firstClassMaxTemp > 1) {
                maxFirstSecondClass = firstClassMaxTemp;
            }
            float wenduDev =0.0f;
            if(minFirstSecondClass > 1 && maxFirstSecondClass > 1){
                wenduDev = maxFirstSecondClass - minFirstSecondClass;
            }
            Integer firstClassSize = firstClassRealDataList.isEmpty() ? 0 : firstClassRealDataList.size();
            Integer secondClassSize = secondClassRealDataList.isEmpty() ? 0 : secondClassRealDataList.size();
            Integer thirdClassSize = thirdClassRealDataList.isEmpty() ? 0 : thirdClassRealDataList.size();

            if(firstClassSize + secondClassSize !=0){
                float xielv = wenduDev / (firstClassSize + secondClassSize);
                zhengzhuRecord.setShangzengXielv(xielv);
            }

            // 二级酒本身斜率
            float secondClassXielv = 0.0f;
            if(secondClassMaxTemp > 1 && secondClassMinTemp > 1){
                secondClassXielv = (secondClassMaxTemp - secondClassMinTemp)/secondClassSize;
                zhengzhuRecord.setShangzengSecondClassXielv(secondClassXielv);
            }

            // 二级三级酒斜率
            float secondThirdClassXielv = 0.0f;
            if(secondClassMinTemp > 1 && thirdClassMaxTemp > 1){
                secondThirdClassXielv =  (thirdClassMaxTemp - secondClassMinTemp) / (secondClassSize + thirdClassSize);
                zhengzhuRecord.setShangzengSecondThirdXielv(secondThirdClassXielv);
            }

            // 三级酒本身斜率
            float thirdClassXielv = 0.0f;
            if(thirdClassMaxTemp > 1 && thirdClassSize != 0){
                thirdClassXielv = (thirdClassMaxTemp - thirdClassMinTemp ) / thirdClassSize;
                zhengzhuRecord.setShangzengThirdClassXielv(thirdClassXielv);
            }
            // 一级三级斜率
            float firstThirdClassXielv = 0.0f;
            if(firstClassMinTemp > 1 && thirdClassMaxTemp > 1){
                firstThirdClassXielv = (thirdClassMaxTemp - firstClassMinTemp) / (firstClassSize + secondClassSize + thirdClassSize);
                zhengzhuRecord.setShangzengfirstThirdXielv(firstThirdClassXielv);
            }

        }
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(zhengzhuRecordList));

        Date minDate2 = winccDataDealCommons.convertStringToDate("2021/01/01 00:00:00");
        List<MesZengguoRecord> mesZengguoRecordList2 = dataManager.load(MesZengguoRecord.class)
                .query("select e from MesZengguoRecord e " +
                        "where (e.shangzengXiaolv is null or e.shangzengXiaolv <= :minXiaolv) " +
                        "and ( e.endTimeTall > :minDate ) " +
                        "and e.phaseEndTimeTotal is not null " +
                        "and (e.jiejiuDurationFirstClass > 0.2 or e.jiejiuDurationSecondClass > 0.2 " +
                        "or e.jiejiuDurationThirdClass > 0.2 or e.jiejiuDurationFeishui > 0.2)")
                .parameter("minXiaolv", 0.0)
                .parameter("minDate", minDate2)
                .list();
        List<MesZengguoRecord> finalRecordToSave = new ArrayList<>();
        for (MesZengguoRecord zengguoRecord : mesZengguoRecordList2) {
            MesZengguoRecord zhengzhuRecord = dataManager.load(MesZengguoRecord.class)
                    .query("select e from MesZengguoRecord e where e.zengguoPhase.phaseNo = :zengguoPhaseNo " +
                            "and e.mesZengguo = :mesZengguo " +
                            "and e.startTimeTotal = :startTimeTotal " +
                            "and e.shangzengXiaolv > :minXiaolv")
                    .parameter("zengguoPhaseNo", 525)
                    .parameter("mesZengguo", zengguoRecord.getMesZengguo())
                    .parameter("startTimeTotal", zengguoRecord.getStartTimeTotal())
                    .parameter("minXiaolv", 0.0)
                    .optional()
                    .orElse(null);
            if (zhengzhuRecord != null) {
                zengguoRecord.setShangzengXiaolv(zhengzhuRecord.getShangzengXiaolv());
                zengguoRecord.setShangzengXielv(zhengzhuRecord.getShangzengXielv());
                zengguoRecord.setShangzengSecondClassXielv(zhengzhuRecord.getShangzengSecondClassXielv());
                zengguoRecord.setShangzengSecondThirdXielv(zhengzhuRecord.getShangzengSecondThirdXielv());
                zengguoRecord.setShangzengThirdClassXielv(zhengzhuRecord.getShangzengThirdClassXielv());
                zengguoRecord.setShangzengfirstThirdXielv(zhengzhuRecord.getShangzengfirstThirdXielv());
                finalRecordToSave.add(zengguoRecord);
            }
        }
        if(!finalRecordToSave.isEmpty()){
            dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(finalRecordToSave));
        }
    }
}
