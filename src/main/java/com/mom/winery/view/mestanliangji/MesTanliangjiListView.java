package com.mom.winery.view.mestanliangji;

import com.mom.winery.entity.MesTanliangji;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:37
 */

@Route(value = "mesTanliangjis", layout = MainView.class)
@ViewController("MesTanliangji.list")
@ViewDescriptor("mes-tanliangji-list-view.xml")
@LookupComponent("mesTanliangjisDataGrid")
@DialogMode(width = "64em")
public class MesTanliangjiListView extends StandardListView<MesTanliangji> {
}
