package com.mom.winery.view.meszengguorecord;

import com.mom.winery.entity.MesZengguoRecord;
import com.mom.winery.entity.MesZengguoUnitProcedure;
import com.mom.winery.view.main.MainView;
import com.mom.winery.view.meszengguounitprocedure.MesZengguoMultiAnalysisView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/6 12:02
 */

@Route(value = "mesZengguoRecordsAnalysis", layout = MainView.class)
@ViewController(id = "MesZengguoRecordAnalysis.list")
@ViewDescriptor(path = "mes-zengguo-record-for-analysis-list-view.xml")
@LookupComponent("mesZengguoRecordsDataGrid")
@DialogMode(width = "64em")
public class MesZengguoRecordAnalysisListView extends StandardListView<MesZengguoRecord> {
    @ViewComponent
    private CollectionLoader<MesZengguoRecord> mesZengguoRecordsDl;
    @ViewComponent
    private DataGrid<MesZengguoRecord> mesZengguoRecordsDataGrid;
    @Autowired
    private Notifications notifications;
    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe
    public void onInit(final InitEvent event) {
        // 将2021年1月1日转换为时间戳
        LocalDateTime startTime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        Date startTimeDate = Date.from(startTime.toInstant(ZoneOffset.ofHours(8)));

        mesZengguoRecordsDl.setParameter("endTimeTall1",startTimeDate);
        mesZengguoRecordsDl.load();

    }

    @Subscribe(id = "zengguoAnalysis", subject = "clickListener")
    public void onZengguoAnalysisClick(final ClickEvent<JmixButton> event) {
        List<MesZengguoRecord> mesZengguoRecordList = mesZengguoRecordsDataGrid.getSelectedItems().stream().toList();
        if(mesZengguoRecordList.isEmpty()){
            notifications.create("请选择需要分析的甑锅执行记录", "未选择需要分析的甑锅执行记录").show();
            return;
        }
        List<Long> recordIds  = mesZengguoRecordList.stream().map(MesZengguoRecord::getId).toList();
        String recordStringIds = recordIds.stream().map(String::valueOf).reduce((a, b) -> a + "," + b).orElse("");
        viewNavigators.view(this, MesZengguoRecordAnalysisiListView.class)
                .withQueryParameters(QueryParameters.of("ids", recordStringIds))
                .withBackwardNavigation(true)
                .navigate();
    }
}


