package com.mom.winery.view.winccrunliangdou;

import com.mom.winery.entity.WinccRunliangdou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:11
 */

@Route(value = "winccRunliangdous", layout = MainView.class)
@ViewController("WinccRunliangdou.list")
@ViewDescriptor("wincc-runliangdou-list-view.xml")
@LookupComponent("winccRunliangdousDataGrid")
@DialogMode(width = "64em")
public class WinccRunliangdouListView extends StandardListView<WinccRunliangdou> {
}
