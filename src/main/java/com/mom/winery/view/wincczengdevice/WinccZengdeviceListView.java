package com.mom.winery.view.wincczengdevice;

import com.mom.winery.entity.WinccZengdevice;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:13
 */

@Route(value = "winccZengdevices", layout = MainView.class)
@ViewController("WinccZengdevice.list")
@ViewDescriptor("wincc-zengdevice-list-view.xml")
@LookupComponent("winccZengdevicesDataGrid")
@DialogMode(width = "64em")
public class WinccZengdeviceListView extends StandardListView<WinccZengdevice> {
}
