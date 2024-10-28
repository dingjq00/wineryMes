package com.mom.winery.view.mesrecyclingbottomwatertank;

import com.mom.winery.entity.MesRecyclingBottomWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:43
 */

@Route(value = "mesRecyclingBottomWaterTanks/:id", layout = MainView.class)
@ViewController("MesRecyclingBottomWaterTank.detail")
@ViewDescriptor("mes-recycling-bottom-water-tank-detail-view.xml")
@EditedEntityContainer("mesRecyclingBottomWaterTankDc")
public class MesRecyclingBottomWaterTankDetailView extends StandardDetailView<MesRecyclingBottomWaterTank> {
}