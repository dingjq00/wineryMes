package com.mom.winery.view.mesrunliangrealtimedata;

import com.mom.winery.entity.MesRunliangRealtimeData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/29 23:00
 */

@Route(value = "mesRunliangRealtimeDatas/:id", layout = MainView.class)
@ViewController("MesRunliangRealtimeData.detail")
@ViewDescriptor("mes-runliang-realtime-data-detail-view.xml")
@EditedEntityContainer("mesRunliangRealtimeDataDc")
public class MesRunliangRealtimeDataDetailView extends StandardDetailView<MesRunliangRealtimeData> {
}
