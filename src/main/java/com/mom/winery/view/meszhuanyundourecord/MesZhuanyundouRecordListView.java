package com.mom.winery.view.meszhuanyundourecord;

import com.mom.winery.entity.MesZhuanyundouRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/3 23:51
 */

@Route(value = "mesZhuanyundouRecords", layout = MainView.class)
@ViewController("MesZhuanyundouRecord.list")
@ViewDescriptor("mes-zhuanyundou-record-list-view.xml")
@LookupComponent("mesZhuanyundouRecordsDataGrid")
@DialogMode(width = "64em")
public class MesZhuanyundouRecordListView extends StandardListView<MesZhuanyundouRecord> {
}
