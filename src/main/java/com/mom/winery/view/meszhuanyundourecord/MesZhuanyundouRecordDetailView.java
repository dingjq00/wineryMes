package com.mom.winery.view.meszhuanyundourecord;

import com.mom.winery.entity.MesZhuanyundouRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/3 23:51
 */

@Route(value = "mesZhuanyundouRecords/:id", layout = MainView.class)
@ViewController("MesZhuanyundouRecord.detail")
@ViewDescriptor("mes-zhuanyundou-record-detail-view.xml")
@EditedEntityContainer("mesZhuanyundouRecordDc")
public class MesZhuanyundouRecordDetailView extends StandardDetailView<MesZhuanyundouRecord> {
}
