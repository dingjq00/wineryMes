package com.mom.winery.view.mescleanwatertank;

import com.mom.winery.entity.MesCleanWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 17:49
 */

@Route(value = "mesCleanWaterTanks", layout = MainView.class)
@ViewController("MesCleanWaterTank.list")
@ViewDescriptor("mes-clean-water-tank-list-view.xml")
@LookupComponent("mesCleanWaterTanksDataGrid")
@DialogMode(width = "64em")
public class MesCleanWaterTankListView extends StandardListView<MesCleanWaterTank> {
}
