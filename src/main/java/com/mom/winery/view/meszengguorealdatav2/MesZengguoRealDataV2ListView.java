package com.mom.winery.view.meszengguorealdatav2;

import com.mom.winery.entity.MesZengguoRealDataV2;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/1 16:42
 */

@Route(value = "mesZengguoRealDataV2s", layout = MainView.class)
@ViewController("MesZengguoRealDataV2.list")
@ViewDescriptor("mes-zengguo-real-data-v2-list-view.xml")
@LookupComponent("mesZengguoRealDataV2sDataGrid")
@DialogMode(width = "64em")
public class MesZengguoRealDataV2ListView extends StandardListView<MesZengguoRealDataV2> {
}
