package com.mom.winery.view.mesareawinetank;

import com.mom.winery.entity.MesAreaWineTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:43
 */

@Route(value = "mesAreaWineTanks/:id", layout = MainView.class)
@ViewController("MesAreaWineTank.detail")
@ViewDescriptor("mes-area-wine-tank-detail-view.xml")
@EditedEntityContainer("mesAreaWineTankDc")
public class MesAreaWineTankDetailView extends StandardDetailView<MesAreaWineTank> {
}
