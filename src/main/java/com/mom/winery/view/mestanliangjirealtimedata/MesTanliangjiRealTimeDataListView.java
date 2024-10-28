package com.mom.winery.view.mestanliangjirealtimedata;

import com.mom.winery.entity.MesTanliangjiRealTimeData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 21:10
 */

@Route(value = "mesTanliangjiRealTimeDatas", layout = MainView.class)
@ViewController("MesTanliangjiRealTimeData.list")
@ViewDescriptor("mes-tanliangji-real-time-data-list-view.xml")
@LookupComponent("mesTanliangjiRealTimeDatasDataGrid")
@DialogMode(width = "64em")
public class MesTanliangjiRealTimeDataListView extends StandardListView<MesTanliangjiRealTimeData> {
}
