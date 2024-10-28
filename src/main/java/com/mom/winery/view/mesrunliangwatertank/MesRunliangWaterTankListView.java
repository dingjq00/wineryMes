package com.mom.winery.view.mesrunliangwatertank;

import com.mom.winery.entity.MesRunliangWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:35
 */

@Route(value = "mesRunliangWaterTanks", layout = MainView.class)
@ViewController("MesRunliangWaterTank.list")
@ViewDescriptor("mes-runliang-water-tank-list-view.xml")
@LookupComponent("mesRunliangWaterTanksDataGrid")
@DialogMode(width = "64em")
public class MesRunliangWaterTankListView extends StandardListView<MesRunliangWaterTank> {
}
