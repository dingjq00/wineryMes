package com.mom.winery.view.meszengguorecord;

import com.mom.winery.entity.MesZengguo;
import com.mom.winery.entity.MesZengguoRecord;
import com.mom.winery.view.main.MainView;
import com.mom.winery.view.meszengguorealdatav2.MesZengguoRealDataAnalysisListView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/26 18:35
 */

@Route(value = "mesZengguoRecordAccordings", layout = MainView.class)
@ViewController(id = "MesZengguoRecordAccording.list")
@ViewDescriptor(path = "mes-zengguo-record-according-list-view.xml")
@LookupComponent("mesZengguoRecordsDataGrid")
@DialogMode(width = "100%", height = "100%")
public class MesZengguoRecordAccordingListView extends StandardListView<MesZengguoRecord> {
    @Autowired
    private DataManager dataManager;

    private MesZengguo mesZengguo;
    private Date startTime;
    @ViewComponent
    private CollectionLoader<MesZengguoRecord> mesZengguoRecordsDl;
    @ViewComponent
    private DataGrid<MesZengguoRecord> mesZengguoRecordsDataGrid;
    @Autowired
    private Notifications notifications;
    @Autowired
    private DialogWindows dialogWindows;

    public void setMesZengguo(MesZengguo mesZengguo) {
        this.mesZengguo = mesZengguo;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public MesZengguo getMesZengguo() {
        return mesZengguo;
    }
    public Date getStartTime() {
        return startTime;
    }



    @Subscribe
    public void onReady(final ReadyEvent event) {
        mesZengguoRecordsDl.setParameter("mesZengguo", mesZengguo);
        mesZengguoRecordsDl.setParameter("startTimeTotal", startTime);
        mesZengguoRecordsDl.load();
    }

    @Subscribe(id = "realdateButton", subject = "clickListener")
    public void onRealdateButtonClick(final ClickEvent<JmixButton> event) {
        MesZengguoRecord record = mesZengguoRecordsDataGrid.getSingleSelectedItem();
        if (record == null) {
            notifications.create("请选择一条记录").show();
            return;
        }
        DialogWindow<MesZengguoRealDataAnalysisListView> window =
                dialogWindows.view(this, MesZengguoRealDataAnalysisListView.class).build();
        window.getView().setMesZengguo(record.getMesZengguo());
        window.getView().setStartTime(record.getPhaseStartTimeTotal());
        window.getView().setEndTime(record.getPhaseEndTimeTotal());
        window.open();
    }

}
