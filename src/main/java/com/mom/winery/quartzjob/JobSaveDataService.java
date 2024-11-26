package com.mom.winery.quartzjob;

import com.mom.winery.entity.*;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobSaveDataService {

    @Autowired
    private DataManager dataManager;

    @Transactional
    public void saveZhuanyundouRecordData(JobConfig jobConfig, List<MesZhuanyundou> areaZhuanyundouList, List<MesZhuanyundouRecord> mesZhuanyundouRecordList, Integer maxWinccId) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(areaZhuanyundouList, mesZhuanyundouRecordList));
        jobConfig.setWinccId(maxWinccId);
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(jobConfig));
    }

    @Transactional
    protected void saveRealData(JobConfig jobConfig, List<MesTanliangRealDataV2> mesTanliangjiRealTimeDataList, List<MesZengguoRealDataV2> mesZengguoRealDataList, List<MesRunliangRealtimeData> mesRunliangRealtimeDataList, List<MesGlobalRealData> mesGlobalRealDataList, List<WinccMoniliang> winccMoniliangList) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true)
                .saving(mesTanliangjiRealTimeDataList)
                .saving(mesZengguoRealDataList)
                .saving(mesRunliangRealtimeDataList)
                .saving(mesGlobalRealDataList)
                .saving(winccMoniliangList)
                .saving(jobConfig));
    }

    @Transactional
    public void saveRunliangData(List<MesRunliangdou> areaRunliangdouList, List<MesRunliangdoudouRecord> mesRunliangdoudouRecordList,List<MesRunliangdouOperation> mesRunliangdouOperationList, JobConfig jobConfig, Integer maxWinccId) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(areaRunliangdouList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesRunliangdoudouRecordList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesRunliangdouOperationList));
        jobConfig.setWinccId(maxWinccId);
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(jobConfig));
    }

    @Transactional
    public void saveTanliangData(List<MesTanliangji> areaTanliangjiList , List<MesTanliangjiRecord> mesTanliangjiRecordList, JobConfig jobConfig, Integer maxWinccId) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(areaTanliangjiList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesTanliangjiRecordList));
        jobConfig.setWinccId(maxWinccId);
        dataManager.save(new SaveContext().setDiscardSaved(true).saving(jobConfig));
    }

    @Transactional
    public void saveZengguoData(JobConfig jobConfig, List<MesZengguo> areaZengguoList, List<MesZengguoRecord> mesZengguoRecordList,List<MesZengguoOperation> mesZengguoOperationList,List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList, Integer maxWinccId) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(areaZengguoList));
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesZengguoRecordList));
//        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesZengguoOperationList));
//        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesZengguoUnitProcedureList));
        jobConfig.setWinccId(maxWinccId);
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(jobConfig));
    }
}
