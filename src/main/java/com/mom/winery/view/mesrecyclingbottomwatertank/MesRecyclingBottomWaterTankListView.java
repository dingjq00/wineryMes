package com.mom.winery.view.mesrecyclingbottomwatertank;

import com.mom.winery.entity.MesRecyclingBottomWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:43
 */

@Route(value = "mesRecyclingBottomWaterTanks", layout = MainView.class)
@ViewController("MesRecyclingBottomWaterTank.list")
@ViewDescriptor("mes-recycling-bottom-water-tank-list-view.xml")
@LookupComponent("mesRecyclingBottomWaterTanksDataGrid")
@DialogMode(width = "64em")
public class MesRecyclingBottomWaterTankListView extends StandardListView<MesRecyclingBottomWaterTank> {
}
