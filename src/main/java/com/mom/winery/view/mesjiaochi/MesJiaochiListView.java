package com.mom.winery.view.mesjiaochi;

import com.mom.winery.entity.MesJiaochi;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:39
 */

@Route(value = "mesJiaochis", layout = MainView.class)
@ViewController("MesJiaochi.list")
@ViewDescriptor("mes-jiaochi-list-view.xml")
@LookupComponent("mesJiaochisDataGrid")
@DialogMode(width = "64em")
public class MesJiaochiListView extends StandardListView<MesJiaochi> {
}
