package com.mom.winery.view.wincczengdata;

import com.mom.winery.entity.WinccZengdata;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:12
 */

@Route(value = "winccZengdatas", layout = MainView.class)
@ViewController("WinccZengdata.list")
@ViewDescriptor("wincc-zengdata-list-view.xml")
@LookupComponent("winccZengdatasDataGrid")
@DialogMode(width = "64em")
public class WinccZengdataListView extends StandardListView<WinccZengdata> {
}
