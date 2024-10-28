package com.mom.winery.view.mestanliangjirecord;

import com.mom.winery.entity.MesTanliangjiRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 15:54
 */

@Route(value = "mesTanliangjiRecords/:id", layout = MainView.class)
@ViewController("MesTanliangjiRecord.detail")
@ViewDescriptor("mes-tanliangji-record-detail-view.xml")
@EditedEntityContainer("mesTanliangjiRecordDc")
public class MesTanliangjiRecordDetailView extends StandardDetailView<MesTanliangjiRecord> {
}
