package com.mom.winery.view.meszengguorecord;

import com.mom.winery.entity.MesZengguoRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 14:06
 */

@Route(value = "mesZengguoRecords/:id", layout = MainView.class)
@ViewController("MesZengguoRecord.detail")
@ViewDescriptor("mes-zengguo-record-detail-view.xml")
@EditedEntityContainer("mesZengguoRecordDc")
public class MesZengguoRecordDetailView extends StandardDetailView<MesZengguoRecord> {
}
