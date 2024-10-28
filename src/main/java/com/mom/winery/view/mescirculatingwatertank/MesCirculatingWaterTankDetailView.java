package com.mom.winery.view.mescirculatingwatertank;

import com.mom.winery.entity.MesCirculatingWaterTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 17:48
 */

@Route(value = "mesCirculatingWaterTanks/:id", layout = MainView.class)
@ViewController("MesCirculatingWaterTank.detail")
@ViewDescriptor("mes-circulating-water-tank-detail-view.xml")
@EditedEntityContainer("mesCirculatingWaterTankDc")
public class MesCirculatingWaterTankDetailView extends StandardDetailView<MesCirculatingWaterTank> {
}
