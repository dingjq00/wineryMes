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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 20:34
 */
@DisallowConcurrentExecution
public class MoniliangJob implements Job {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    @Authenticated
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<JobConfig> jobConfigList = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mainPhase = :mainPhase")
                .parameter("mainPhase", EnumProcessMainPhase.MONILIANG)
                .list();

        for (JobConfig jobConfig : jobConfigList) {
            MesArea mesArea = jobConfig.getMesArea();
            Integer areaNo = jobConfig.getMesArea().getAreaCode();
            Integer currentWinccId = jobConfig.getWinccId();
            List<WinccMoniliang> winccMoniliangList = dataManager.load(WinccMoniliang.class)
                    .query("select e from WinccMoniliang e " +
                            "where e.areaNo = :areaNo " +
                            "and e.winccId > :currentWinccId " +
                            "and e.isDealed is null ")
                    .parameter("areaNo", areaNo)
                    .parameter("currentWinccId", currentWinccId)
                    .list();
            Integer maxWinccId = winccMoniliangList.stream().map(WinccMoniliang::getWinccId).max(Integer::compareTo).orElse(0);
            List<MesTanliangjiRealTimeData> mesTanliangjiRealTimeDataList = new ArrayList<>();

            for (WinccMoniliang winccMoniliang : winccMoniliangList) {
                MesTanliangjiRealTimeData mesTanliangjiRealTimeData = dataManager.create(MesTanliangjiRealTimeData.class);
                Date winccUpdateTime = winccMoniliang.getStarttime();
                mesTanliangjiRealTimeData.setWinccUpdateTime(winccUpdateTime);

                mesTanliangjiRealTimeData.setWinccId(winccMoniliang.getWinccId());

                String comment = winccMoniliang.getComment();
                String[] commentParts = comment.split(",");


                // 余重_1#摊晾给料机//余重_2#摊晾给料机//余重_1#曲粉暂存仓//余重_2#曲粉暂存仓
                mesTanliangjiRealTimeData.setTanliangGeiliaojiYuzhong1( Float.parseFloat(commentParts[12]));
                mesTanliangjiRealTimeData.setTanliangGeiliaojiYuzhong2( Float.parseFloat(commentParts[13]));
                mesTanliangjiRealTimeData.setQufeiZancuncangYuzhong1( Float.parseFloat(commentParts[14]));
                mesTanliangjiRealTimeData.setQufeiZancuncangYuzhong2( Float.parseFloat(commentParts[15]));
                // 21 流速_1#摊晾给料机   22 流速_2#摊晾给料机   23 流速_1#曲粉   24 流速_2#曲粉
                mesTanliangjiRealTimeData.setTangliangGeiliaojiLiusu1( Float.parseFloat(commentParts[21]));
                mesTanliangjiRealTimeData.setTangliangGeiliaojiLiusu2( Float.parseFloat(commentParts[22]));
                mesTanliangjiRealTimeData.setQufenLiusu1( Float.parseFloat(commentParts[23]));
                mesTanliangjiRealTimeData.setQufenLiusu2( Float.parseFloat(commentParts[24]));
                //134 1#摊晾中部温度； 135 1#摊晾出口温度； 136 2#摊晾中部温度； 137 2#摊晾出口温度
                mesTanliangjiRealTimeData.setTanliangZhongbuTemp1( Float.parseFloat(commentParts[134]));
                mesTanliangjiRealTimeData.setTanliangChukouTemp1( Float.parseFloat(commentParts[135]));
                mesTanliangjiRealTimeData.setTanliangZhongbuTemp2( Float.parseFloat(commentParts[136]));
                mesTanliangjiRealTimeData.setTanliangChukouTemp2( Float.parseFloat(commentParts[137]));

                mesTanliangjiRealTimeData.setMesArea(mesArea);
                mesTanliangjiRealTimeDataList.add(mesTanliangjiRealTimeData);
                winccMoniliang.setIsDealed(1);
            }
            jobConfig.setWinccId(maxWinccId);
            saveData(jobConfig, mesTanliangjiRealTimeDataList, winccMoniliangList);
        }
    }

    @Transactional
    public void saveData(JobConfig jobConfig, List<MesTanliangjiRealTimeData> mesTanliangjiRealTimeDataList, List<WinccMoniliang> winccMoniliangList) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesTanliangjiRealTimeDataList).saving(winccMoniliangList).saving(jobConfig));
    }
}
