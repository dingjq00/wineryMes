package com.mom.winery.view.messhopfloor;

import com.mom.winery.entity.MesShopfloor;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 17:57
 */

@Route(value = "mesShopfloors/:id", layout = MainView.class)
@ViewController("MesShopfloor.detail")
@ViewDescriptor("mes-shopfloor-detail-view.xml")
@EditedEntityContainer("mesShopfloorDc")
public class MesShopfloorDetailView extends StandardDetailView<MesShopfloor> {
}
