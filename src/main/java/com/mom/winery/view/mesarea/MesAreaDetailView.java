package com.mom.winery.view.mesarea;

import com.mom.winery.entity.MesArea;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:45
 */

@Route(value = "mesAreas/:id", layout = MainView.class)
@ViewController("MesArea.detail")
@ViewDescriptor("mes-area-detail-view.xml")
@EditedEntityContainer("mesAreaDc")
public class MesAreaDetailView extends StandardDetailView<MesArea> {
}
