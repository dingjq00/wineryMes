package com.mom.winery.view.wincczhuanyundou;

import com.mom.winery.entity.WinccZhuanyundou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:17
 */

@Route(value = "winccZhuanyundous", layout = MainView.class)
@ViewController("WinccZhuanyundou.list")
@ViewDescriptor("wincc-zhuanyundou-list-view.xml")
@LookupComponent("winccZhuanyundousDataGrid")
@DialogMode(width = "64em")
public class WinccZhuanyundouListView extends StandardListView<WinccZhuanyundou> {
}
