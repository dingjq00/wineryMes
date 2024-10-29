package com.mom.winery.view.mesrunliangrealtimedata;

import com.mom.winery.entity.MesRunliangRealtimeData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/29 23:00
 */

@Route(value = "mesRunliangRealtimeDatas", layout = MainView.class)
@ViewController("MesRunliangRealtimeData.list")
@ViewDescriptor("mes-runliang-realtime-data-list-view.xml")
@LookupComponent("mesRunliangRealtimeDatasDataGrid")
@DialogMode(width = "64em")
public class MesRunliangRealtimeDataListView extends StandardListView<MesRunliangRealtimeData> {
}
