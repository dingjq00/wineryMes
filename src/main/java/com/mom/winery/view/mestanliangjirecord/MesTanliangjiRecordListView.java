package com.mom.winery.view.mestanliangjirecord;

import com.mom.winery.entity.MesTanliangjiRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 15:54
 */

@Route(value = "mesTanliangjiRecords", layout = MainView.class)
@ViewController("MesTanliangjiRecord.list")
@ViewDescriptor("mes-tanliangji-record-list-view.xml")
@LookupComponent("mesTanliangjiRecordsDataGrid")
@DialogMode(width = "64em")
public class MesTanliangjiRecordListView extends StandardListView<MesTanliangjiRecord> {
}
