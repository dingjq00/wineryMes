package com.mom.winery.view.messhopfloorwinetank;

import com.mom.winery.entity.MesShopfloorWineTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 15:35
 */

@Route(value = "mesShopfloorWineTanks/:id", layout = MainView.class)
@ViewController("MesShopfloorWineTank.detail")
@ViewDescriptor("mes-shopfloor-wine-tank-detail-view.xml")
@EditedEntityContainer("mesShopfloorWineTankDc")
public class MesShopfloorWineTankDetailView extends StandardDetailView<MesShopfloorWineTank> {
}
