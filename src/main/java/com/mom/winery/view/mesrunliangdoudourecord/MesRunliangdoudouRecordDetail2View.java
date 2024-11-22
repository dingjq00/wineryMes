package com.mom.winery.view.mesrunliangdoudourecord;

import com.mom.winery.entity.MesRunliangdoudouRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/21 15:13
 */

@Route(value = "mesRunliangdoudouRecords2/:id", layout = MainView.class)
@ViewController(id = "MesRunliangdoudouRecord2.detail")
@ViewDescriptor(path = "mes-runliangdoudou-record-detail2-view.xml")
@EditedEntityContainer("mesRunliangdoudouRecordDc")
public class MesRunliangdoudouRecordDetail2View extends StandardDetailView<MesRunliangdoudouRecord> {
}
