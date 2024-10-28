package com.mom.winery.view.mescirculatingwatertank;

import com.mom.winery.entity.MesCirculatingWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 17:48
 */

@Route(value = "mesCirculatingWaterTanks", layout = MainView.class)
@ViewController("MesCirculatingWaterTank.list")
@ViewDescriptor("mes-circulating-water-tank-list-view.xml")
@LookupComponent("mesCirculatingWaterTanksDataGrid")
@DialogMode(width = "64em")
public class MesCirculatingWaterTankListView extends StandardListView<MesCirculatingWaterTank> {
}
