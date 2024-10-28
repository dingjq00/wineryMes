package com.mom.winery.view.wincchangche;

import com.mom.winery.entity.WinccHangche;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 10:56
 */

@Route(value = "winccHangches", layout = MainView.class)
@ViewController("WinccHangche.list")
@ViewDescriptor("wincc-hangche-list-view.xml")
@LookupComponent("winccHangchesDataGrid")
@DialogMode(width = "64em")
public class WinccHangcheListView extends StandardListView<WinccHangche> {
}
