package com.mom.winery.quartzjob;

import com.mom.winery.app.WinccDataDealCommons;
import com.mom.winery.entity.MesArea;
import com.mom.winery.entity.MesZengSumData;
import com.mom.winery.entity.MesZengguo;
import com.mom.winery.entity.MesZengguoRecord;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/14 09:18
 */
@DisallowConcurrentExecution
public class SumDataToZengguoRecordJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(SumDataToZengguoRecordJob.class);
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    @Autowired
    private JobSaveDataService jobSaveDataService;

    @Authenticated
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Date minDate = winccDataDealCommons.convertStringToDate("2024/10/15 00:01:00");
        List<MesArea> mesAreaList = dataManager.load(MesArea.class)
                .query("select e from MesArea e ")
                .list();
        List<MesZengSumData> mesZengSumDataList = dataManager.load(MesZengSumData.class)
                .query("select e from MesZengSumData e ")
                .list();
        for (MesArea mesArea : mesAreaList) {
            List<MesZengguoRecord> mesZengguoRecordList = dataManager.load(MesZengguoRecord.class)
                    .query("select e from MesZengguoRecord e where e.sumDataRecord is null " +
                            "and e.phaseEndTimeTotal is not null " +
                            "and e.endTimeTall >= :minDate " +
                            "and e.mesZengguo.mesArea = :mesArea " +
                            " order by e.phaseStartWinccId asc")
                    .parameter("minDate", minDate)
                    .parameter("mesArea", mesArea)
                    .list();
            List<MesZengguoRecord> zengguoRecordToSave = new ArrayList<>();
            for (MesZengguoRecord zengguoRecord : mesZengguoRecordList) {
                List<MesZengSumData> mesZengSumDatas = mesZengSumDataList.stream()
                        .filter(e ->e.getResourceZengguo() != null && e.getResourceZengguo().equals(zengguoRecord.getMesZengguo())
                                && e.getZengStartTime().equals(zengguoRecord.getStartTimeTotal()))
                        .toList();
                if (!mesZengSumDatas.isEmpty()) {
                    MesZengSumData mesZengSumData = mesZengSumDatas.getFirst();
                    zengguoRecord.setSumDataRecord(mesZengSumData);
                    zengguoRecordToSave.add(zengguoRecord);
                }
            }
            dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(zengguoRecordToSave));
        }
    }
}
