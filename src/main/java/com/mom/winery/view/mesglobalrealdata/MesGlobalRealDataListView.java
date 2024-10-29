package com.mom.winery.view.mesglobalrealdata;

import com.mom.winery.entity.MesGlobalRealData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/29 23:10
 */

@Route(value = "mesGlobalRealDatas", layout = MainView.class)
@ViewController("MesGlobalRealData.list")
@ViewDescriptor("mes-global-real-data-list-view.xml")
@LookupComponent("mesGlobalRealDatasDataGrid")
@DialogMode(width = "64em")
public class MesGlobalRealDataListView extends StandardListView<MesGlobalRealData> {
}
