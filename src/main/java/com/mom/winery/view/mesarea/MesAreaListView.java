package com.mom.winery.view.mesarea;

import com.mom.winery.entity.MesArea;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:45
 */

@Route(value = "mesAreas", layout = MainView.class)
@ViewController("MesArea.list")
@ViewDescriptor("mes-area-list-view.xml")
@LookupComponent("mesAreasDataGrid")
@DialogMode(width = "64em")
public class MesAreaListView extends StandardListView<MesArea> {
}
