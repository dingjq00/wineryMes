package com.mom.winery.view.winccmoniliang;

import com.mom.winery.entity.WinccMoniliang;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:10
 */

@Route(value = "winccMoniliangs", layout = MainView.class)
@ViewController("WinccMoniliang.list")
@ViewDescriptor("wincc-moniliang-list-view.xml")
@LookupComponent("winccMoniliangsDataGrid")
@DialogMode(width = "64em")
public class WinccMoniliangListView extends StandardListView<WinccMoniliang> {
}
