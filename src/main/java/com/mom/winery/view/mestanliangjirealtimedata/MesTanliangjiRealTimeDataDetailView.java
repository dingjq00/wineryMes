package com.mom.winery.view.mestanliangjirealtimedata;

import com.mom.winery.entity.MesTanliangjiRealTimeData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 21:10
 */

@Route(value = "mesTanliangjiRealTimeDatas/:id", layout = MainView.class)
@ViewController("MesTanliangjiRealTimeData.detail")
@ViewDescriptor("mes-tanliangji-real-time-data-detail-view.xml")
@EditedEntityContainer("mesTanliangjiRealTimeDataDc")
public class MesTanliangjiRealTimeDataDetailView extends StandardDetailView<MesTanliangjiRealTimeData> {
}
