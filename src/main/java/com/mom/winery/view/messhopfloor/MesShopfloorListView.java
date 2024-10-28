package com.mom.winery.view.messhopfloor;

import com.mom.winery.entity.MesShopfloor;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 17:57
 */

@Route(value = "mesShopfloors", layout = MainView.class)
@ViewController("MesShopfloor.list")
@ViewDescriptor("mes-shopfloor-list-view.xml")
@LookupComponent("mesShopfloorsDataGrid")
@DialogMode(width = "64em")
public class MesShopfloorListView extends StandardListView<MesShopfloor> {
}
