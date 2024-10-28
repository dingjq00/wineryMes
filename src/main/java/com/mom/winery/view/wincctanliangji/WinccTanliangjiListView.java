package com.mom.winery.view.wincctanliangji;

import com.mom.winery.entity.WinccTanliangji;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:11
 */

@Route(value = "winccTanliangjis", layout = MainView.class)
@ViewController("WinccTanliangji.list")
@ViewDescriptor("wincc-tanliangji-list-view.xml")
@LookupComponent("winccTanliangjisDataGrid")
@DialogMode(width = "64em")
public class WinccTanliangjiListView extends StandardListView<WinccTanliangji> {
}
