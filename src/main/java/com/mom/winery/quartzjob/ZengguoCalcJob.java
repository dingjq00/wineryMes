package com.mom.winery.quartzjob;

import com.mom.winery.app.WinccDataDealCommons;
import com.mom.winery.entity.MesZenggouPhaseConfig;
import com.mom.winery.entity.MesZengguo;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/14 12:18
 */
@DisallowConcurrentExecution
public class ZengguoCalcJob implements Job {
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
        List<MesZengguoRecord> mesZengguoRecordList = dataManager.load(MesZengguoRecord.class)
            .query("select e from MesZengguoRecord e " +
                    "where (e.shangzengXiaolv is null or e.shangzengXiaolv <= :minXiaolv) " +
                    "and e.endTimeTall > :minDate " +
                    "and e.jiejiuDurationThirdClass >= 1.5 " )
            .parameter("minXiaolv", 0.0)
            .parameter("minDate", minDate)
            .maxResults(500)
            .list();
        if(mesZengguoRecordList.isEmpty()){
            return;
        }
//        for (MesZengguoRecord zengguoRecord : mesZengguoRecordList) {
//            dataManager.load(MesZengguoRecord.class)
//                    .query("select e from MesZengguoRecord e where e.mesZengguo = :mesZengguo " +
//                            "and e.startTimeTotal = :startToal " +
//                            "and  e.zengguoPhase.phaseNo = :zhengzhuPhaseNo")
//                    .parameter("mesZengguo", zengguoRecord.getMesZengguo())
//                    .parameter("startToal", zengguoRecord.getStartTimeTotal())
//                    .parameter("zhengzhuPhaseNo", 525)
//                    .optional().ifPresent(notHaveZhengzhuRecord -> mesZengguoRecordList.remove(zengguoRecord));
//        }
//        if(mesZengguoRecordList.isEmpty()){
//            return;
//        }

        List<Integer> liujiuList = Arrays.asList(520,521,522,523,524);
        List<MesZenggouPhaseConfig> zengguoPhaseConfigList = dataManager.load(MesZenggouPhaseConfig.class)
                .query("select e from MesZenggouPhaseConfig e where e.phaseNo in :liujiuList order by e.phaseNo")
                .parameter("liujiuList", liujiuList)
                .list();
        for (MesZengguoRecord zengguoRecord : mesZengguoRecordList) {
            List<MesZengguoRecord> zengguoRecordList = dataManager.load(MesZengguoRecord.class)
                    .query("select e from MesZengguoRecord e where e.mesZengguo = :mesZengguo " +
                            "and e.startTimeTotal = :zengStartTime " +
                            "and e.zengguoPhase in :zengguoPhase ")
                    .parameter("mesZengguo", zengguoRecord.getMesZengguo())
                    .parameter("zengStartTime", zengguoRecord.getStartTimeTotal())
                    .parameter("zengguoPhase", zengguoPhaseConfigList)
                    .list();
            Date minDate1 = zengguoRecordList.stream()
                    .map(MesZengguoRecord::getPhaseStartTimeTotal)
                    .min(Date::compareTo)
                    .orElse(null);
            Date maxDate1 = zengguoRecordList.stream()
                    .map(MesZengguoRecord::getPhaseEndTimeTotal)
                    .max(Date::compareTo)
                    .orElse(null);
            List<MesZengguoRealDataV2> zengguoRealDataList = dataManager.load(MesZengguoRealDataV2.class)
                    .query("select e from MesZengguoRealDataV2 e where e.mesZengguo = :mesZengguo " +
                            "and e.winccUpdateTime >= :minDate1 " +
                            "and e.winccUpdateTime < :maxDate1")
                    .parameter("mesZengguo", zengguoRecord.getMesZengguo())
                    .parameter("minDate1", minDate1)
                    .parameter("maxDate1", maxDate1)
                    .list();
            if(zengguoRealDataList.isEmpty()){
                continue;
            }
            Date maxRealDate = zengguoRealDataList.stream()
                    .map(MesZengguoRealDataV2::getWinccUpdateTime)
                    .max(Date::compareTo)
                    .orElse(null);
            // 如果maxRealDate晚于maxDate1超过十分钟，则 continue;
            if(maxRealDate != null && maxDate1 != null){
                long diff = maxRealDate.getTime() - maxDate1.getTime();
                if(diff > 10 * 60 * 1000){
                    continue;
                }
            }

            MesZengguoRecord thirdClassRecord = zengguoRecordList.stream()
                    .filter(e -> e.getZengguoPhase().getPhaseNo() == 523)
                    .findFirst()
                    .orElse(null);
            List<MesZengguoRealDataV2> thirdClassRealDataList = null;
            float thirdClassMaxTemp;
            float thirdClassMinTemp = 0.0f;
            if(thirdClassRecord != null){
                thirdClassRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(thirdClassRecord.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(thirdClassRecord.getPhaseEndTimeTotal()) < 0)
                        .toList();
                thirdClassMaxTemp = thirdClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);
                thirdClassMinTemp = thirdClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .min(Float::compareTo)
                        .orElse(0.0f);
            } else {
                thirdClassMaxTemp = 0.0f;
            }

            MesZengguoRecord jiuweiRecord = zengguoRecordList.stream()
                    .filter(e -> e.getZengguoPhase().getPhaseNo() == 524)
                    .findFirst()
                    .orElse(null);
            List<MesZengguoRealDataV2> jiuweiRealDataList = null;
            float jiuweiMaxTemp;
            float jiuweiMinTemp;
            if(jiuweiRecord != null){
                jiuweiRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(jiuweiRecord.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(jiuweiRecord.getPhaseEndTimeTotal()) < 0)
                        .filter(e-> e.getGuoniWendu() <=100)
                        .toList();
                jiuweiMaxTemp = jiuweiRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);
                jiuweiMinTemp = jiuweiRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .min(Float::compareTo)
                        .orElse(0.0f);
            } else {
                jiuweiMaxTemp = 0.0f;
                jiuweiMinTemp = 0.0f;
            }

            MesZengguoRecord jiutouRecord = zengguoRecordList.stream()
                    .filter(e -> e.getZengguoPhase().getPhaseNo() == 520)
                    .findFirst()
                    .orElse(null);
            List<MesZengguoRealDataV2> jiutouRealDataList = null;
            if(jiutouRecord != null){
                jiutouRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(jiutouRecord.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(jiutouRecord.getPhaseEndTimeTotal()) < 0)
                        .toList();
            }

            MesZengguoRecord firstClassRecord = zengguoRecordList.stream()
                    .filter(e -> e.getZengguoPhase().getPhaseNo() == 521)
                    .findFirst()
                    .orElse(null);
            List<MesZengguoRealDataV2> firstClassRealDataList = null;
            float firstClassMaxTemp = 0.0f;
            float firstClassMinTemp = 0.0f;
            if(firstClassRecord != null){
                firstClassRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(firstClassRecord.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(firstClassRecord.getPhaseEndTimeTotal()) < 0)
                        .toList();
                firstClassMaxTemp = firstClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);
                firstClassMinTemp = firstClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .min(Float::compareTo)
                        .orElse(0.0f);
            }

            MesZengguoRecord secondClassRecord = zengguoRecordList.stream()
                    .filter(e -> e.getZengguoPhase().getPhaseNo() == 522)
                    .findFirst()
                    .orElse(null);
            List<MesZengguoRealDataV2> secondClassRealDataList = null;
            float secondClassMaxTemp = 0.0f;
            float secondClassMinTemp = 0.0f;
            if(secondClassRecord != null){
                secondClassRealDataList = zengguoRealDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(secondClassRecord.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(secondClassRecord.getPhaseEndTimeTotal()) < 0)
                        .toList();
                secondClassMaxTemp = secondClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .max(Float::compareTo)
                        .orElse(0.0f);
                secondClassMinTemp = secondClassRealDataList.stream()
                        .map(MesZengguoRealDataV2::getGuoniWendu)
                        .min(Float::compareTo)
                        .orElse(0.0f);
            }

            if(thirdClassMaxTemp > 1 ){
                //一级酒面积
                Float firstClassArea = 0.0f;
                List<Float> firstClassAreaList = null;
                if (firstClassRealDataList != null) {
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
                if (secondClassRealDataList != null) {
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
                Float thirdClassArea = 0.0f;
                List<Float> thirdClassAreaList = null;
                if (thirdClassRealDataList != null) {
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
                if (jiuweiRealDataList != null) {
                    jiuweiAreaList = jiuweiRealDataList.stream()
                            .map(MesZengguoRealDataV2::getGuoniWendu)
                            .toList();
                    // 用酒尾最大温度减去每一个的值，如果值<0,则为0,然后求和
                    jiuweiAreaFull = jiuweiAreaList.stream()
                            .map(e -> jiuweiMaxTemp - e)
                            .map(e -> e < 0 ? 0 : e)
                            .reduce(Float::sum)
                            .orElse(0.0f);
                }

                // 计算装甑效率
                float firstSecondThirdToalArea = firstClassArea + secondClassArea + thirdClassArea;
                float zhuangzengXiaolv = firstSecondThirdToalArea / (firstSecondThirdToalArea+ jiuweiAreaFull);
                zhuangzengXiaolv = zhuangzengXiaolv * 100;
                zengguoRecord.setShangzengXiaolv(zhuangzengXiaolv);
            }

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
            Integer firstClassSize = firstClassRealDataList == null ? 0 : firstClassRealDataList.size();
            Integer secondClassSize = secondClassRealDataList == null ? 0 : secondClassRealDataList.size();
            Integer thirdClassSize = thirdClassRealDataList ==null ? 0 : thirdClassRealDataList.size();
            // 计算斜率
            if(firstClassSize + secondClassSize !=0){
                float xielv = wenduDev / (firstClassSize + secondClassSize);
                zengguoRecord.setShangzengXielv(xielv);
            }

            // 二级酒本身斜率
            float secondClassXielv = 0.0f;
            if(secondClassMaxTemp > 1 && secondClassMinTemp > 1){
                secondClassXielv = (secondClassMaxTemp - secondClassMinTemp)/secondClassSize;
                zengguoRecord.setShangzengSecondClassXielv(secondClassXielv);
            }

            // 二级三级酒斜率
            float secondThirdClassXielv = 0.0f;
            if(secondClassMinTemp > 1 && thirdClassMaxTemp > 1){
                secondThirdClassXielv =  (thirdClassMaxTemp - secondClassMinTemp) / (secondClassSize + thirdClassSize);
                zengguoRecord.setShangzengSecondThirdXielv(secondThirdClassXielv);
            }

            // 三级酒本身斜率
            float thirdClassXielv = 0.0f;
            if(thirdClassMaxTemp > 1 && thirdClassSize != 0){
                thirdClassXielv = (thirdClassMaxTemp - thirdClassMinTemp ) / thirdClassSize;
                zengguoRecord.setShangzengThirdClassXielv(thirdClassXielv);
            }

            // 一级三级斜率
            float firstThirdClassXielv = 0.0f;
            if(firstClassMinTemp > 1 && thirdClassMaxTemp > 1){
                firstThirdClassXielv = (thirdClassMaxTemp - firstClassMinTemp) / (firstClassSize + secondClassSize + thirdClassSize);
                zengguoRecord.setShangzengfirstThirdXielv(firstThirdClassXielv);
            }
        }
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesZengguoRecordList));
    }
}
