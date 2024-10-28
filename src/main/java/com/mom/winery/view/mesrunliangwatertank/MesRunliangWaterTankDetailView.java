package com.mom.winery.view.mesrunliangwatertank;

import com.mom.winery.entity.MesRunliangWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:35
 */

@Route(value = "mesRunliangWaterTanks/:id", layout = MainView.class)
@ViewController("MesRunliangWaterTank.detail")
@ViewDescriptor("mes-runliang-water-tank-detail-view.xml")
@EditedEntityContainer("mesRunliangWaterTankDc")
public class MesRunliangWaterTankDetailView extends StandardDetailView<MesRunliangWaterTank> {
}
