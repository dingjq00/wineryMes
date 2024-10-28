package com.mom.winery.view.mesrunliangdoudourecord;

import com.mom.winery.entity.MesRunliangdoudouRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/26 19:51
 */

@Route(value = "mesRunliangdoudouRecords", layout = MainView.class)
@ViewController("MesRunliangdoudouRecord.list")
@ViewDescriptor("mes-runliangdoudou-record-list-view.xml")
@LookupComponent("mesRunliangdoudouRecordsDataGrid")
@DialogMode(width = "64em")
public class MesRunliangdoudouRecordListView extends StandardListView<MesRunliangdoudouRecord> {
}
