package com.mom.winery.view.meszhuangzengdvice;

import com.mom.winery.entity.MesZhuangzengDvice;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:35
 */

@Route(value = "mesZhuangzengDvices", layout = MainView.class)
@ViewController("MesZhuangzengDvice.list")
@ViewDescriptor("mes-zhuangzeng-dvice-list-view.xml")
@LookupComponent("mesZhuangzengDvicesDataGrid")
@DialogMode(width = "64em")
public class MesZhuangzengDviceListView extends StandardListView<MesZhuangzengDvice> {
}
