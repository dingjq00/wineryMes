package com.mom.winery.view.mesrunliangdoudourecord;

import com.mom.winery.entity.MesRunliangdoudouRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/21 15:13
 */

@Route(value = "mesRunliangdoudouRecords2", layout = MainView.class)
@ViewController(id = "MesRunliangdoudouRecord2.list")
@ViewDescriptor(path = "mes-runliangdoudou-record-list2-view.xml")
@LookupComponent("mesRunliangdoudouRecordsDataGrid")
@DialogMode(width = "64em")
public class MesRunliangdoudouRecordList2View extends StandardListView<MesRunliangdoudouRecord> {
}
