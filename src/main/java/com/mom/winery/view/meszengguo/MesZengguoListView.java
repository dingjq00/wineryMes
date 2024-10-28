package com.mom.winery.view.meszengguo;

import com.mom.winery.entity.MesZengguo;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:36
 */

@Route(value = "mesZengguos", layout = MainView.class)
@ViewController("MesZengguo.list")
@ViewDescriptor("mes-zengguo-list-view.xml")
@LookupComponent("mesZengguosDataGrid")
@DialogMode(width = "64em")
public class MesZengguoListView extends StandardListView<MesZengguo> {
}
