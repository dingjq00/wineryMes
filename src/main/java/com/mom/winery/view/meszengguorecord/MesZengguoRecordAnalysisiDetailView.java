package com.mom.winery.view.meszengguorecord;

import com.mom.winery.entity.MesZengguo;
import com.mom.winery.entity.MesZengguoRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;

import java.util.Date;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/6 12:02
 */

@Route(value = "mesZengguoRecordsAnalysis/:id", layout = MainView.class)
@ViewController(id = "MesZengguoRecordAnalysisi.detail")
@ViewDescriptor(path = "mes-zengguo-record-for-analysis-detail-view.xml")
@EditedEntityContainer("mesZengguoRecordDc")
public class MesZengguoRecordAnalysisiDetailView extends StandardDetailView<MesZengguoRecord> {
    @ViewComponent
    private CollectionLoader<MesZengguoRecord> mesZengguoRecordsDl;

    @Subscribe
    public void onInit(final InitEvent event) {

    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        MesZengguoRecord finalRecord = getEditedEntity();
        Date finalRecordStartTime = finalRecord.getStartTimeTotal();
        Date finalRecordEndtime = finalRecord.getEndTimeTall();
        MesZengguo mesZengguo = finalRecord.getMesZengguo();
        mesZengguoRecordsDl.setParameter("mesZengguo1",mesZengguo);
        mesZengguoRecordsDl.setParameter("startTimeTotal1",finalRecordStartTime);
        mesZengguoRecordsDl.setParameter("endTimeTall1",finalRecordEndtime);
        mesZengguoRecordsDl.load();
    }


}
