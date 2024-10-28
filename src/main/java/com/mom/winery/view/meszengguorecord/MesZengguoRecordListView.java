package com.mom.winery.view.meszengguorecord;

import com.mom.winery.entity.MesZengguoRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 14:06
 */

@Route(value = "mesZengguoRecords", layout = MainView.class)
@ViewController("MesZengguoRecord.list")
@ViewDescriptor("mes-zengguo-record-list-view.xml")
@LookupComponent("mesZengguoRecordsDataGrid")
@DialogMode(width = "64em")
public class MesZengguoRecordListView extends StandardListView<MesZengguoRecord> {
}
