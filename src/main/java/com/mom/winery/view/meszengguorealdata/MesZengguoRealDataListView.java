package com.mom.winery.view.meszengguorealdata;

import com.mom.winery.entity.MesZengguoRealData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/29 08:56
 */

@Route(value = "mesZengguoRealDatas", layout = MainView.class)
@ViewController("MesZengguoRealData.list")
@ViewDescriptor("mes-zengguo-real-data-list-view.xml")
@LookupComponent("mesZengguoRealDatasDataGrid")
@DialogMode(width = "64em")
public class MesZengguoRealDataListView extends StandardListView<MesZengguoRealData> {
}
