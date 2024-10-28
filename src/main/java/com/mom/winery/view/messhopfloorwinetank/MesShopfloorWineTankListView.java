package com.mom.winery.view.messhopfloorwinetank;

import com.mom.winery.entity.MesShopfloorWineTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 15:35
 */

@Route(value = "mesShopfloorWineTanks", layout = MainView.class)
@ViewController("MesShopfloorWineTank.list")
@ViewDescriptor("mes-shopfloor-wine-tank-list-view.xml")
@LookupComponent("mesShopfloorWineTanksDataGrid")
@DialogMode(width = "64em")
public class MesShopfloorWineTankListView extends StandardListView<MesShopfloorWineTank> {
}
