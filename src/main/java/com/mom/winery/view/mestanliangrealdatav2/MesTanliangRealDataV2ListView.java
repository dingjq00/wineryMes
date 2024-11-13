package com.mom.winery.view.mestanliangrealdatav2;

import com.mom.winery.entity.MesTanliangRealDataV2;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/1 16:41
 */

@Route(value = "mesTanliangRealDataV2s", layout = MainView.class)
@ViewController("MesTanliangRealDataV2.list")
@ViewDescriptor("mes-tanliang-real-data-v2-list-view.xml")
@LookupComponent("mesTanliangRealDataV2sDataGrid")
@DialogMode(width = "64em")
public class MesTanliangRealDataV2ListView extends StandardListView<MesTanliangRealDataV2> {
}
