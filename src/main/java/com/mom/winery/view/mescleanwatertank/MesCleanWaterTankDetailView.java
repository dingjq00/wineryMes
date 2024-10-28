package com.mom.winery.view.mescleanwatertank;

import com.mom.winery.entity.MesCleanWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 17:49
 */

@Route(value = "mesCleanWaterTanks/:id", layout = MainView.class)
@ViewController("MesCleanWaterTank.detail")
@ViewDescriptor("mes-clean-water-tank-detail-view.xml")
@EditedEntityContainer("mesCleanWaterTankDc")
public class MesCleanWaterTankDetailView extends StandardDetailView<MesCleanWaterTank> {
}
