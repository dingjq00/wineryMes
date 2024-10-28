package com.mom.winery.view.meszhuanyundou;

import com.mom.winery.entity.MesZhuanyundou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:34
 */

@Route(value = "mesZhuanyundous", layout = MainView.class)
@ViewController("MesZhuanyundou.list")
@ViewDescriptor("mes-zhuanyundou-list-view.xml")
@LookupComponent("mesZhuanyundousDataGrid")
@DialogMode(width = "64em")
public class MesZhuanyundouListView extends StandardListView<MesZhuanyundou> {
}
