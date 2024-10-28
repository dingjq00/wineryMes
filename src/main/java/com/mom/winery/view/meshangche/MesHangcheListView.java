package com.mom.winery.view.meshangche;

import com.mom.winery.entity.MesHangche;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:57
 */

@Route(value = "mesHangches", layout = MainView.class)
@ViewController("MesHangche.list")
@ViewDescriptor("mes-hangche-list-view.xml")
@LookupComponent("mesHangchesDataGrid")
@DialogMode(width = "64em")
public class MesHangcheListView extends StandardListView<MesHangche> {
}
