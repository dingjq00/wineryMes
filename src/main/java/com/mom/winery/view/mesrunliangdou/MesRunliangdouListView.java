package com.mom.winery.view.mesrunliangdou;

import com.mom.winery.entity.MesRunliangdou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:38
 */

@Route(value = "mesRunliangdous", layout = MainView.class)
@ViewController("MesRunliangdou.list")
@ViewDescriptor("mes-runliangdou-list-view.xml")
@LookupComponent("mesRunliangdousDataGrid")
@DialogMode(width = "64em")
public class MesRunliangdouListView extends StandardListView<MesRunliangdou> {
}
