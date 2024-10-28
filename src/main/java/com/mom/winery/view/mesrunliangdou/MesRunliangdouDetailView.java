package com.mom.winery.view.mesrunliangdou;

import com.mom.winery.entity.MesRunliangdou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:38
 */

@Route(value = "mesRunliangdous/:id", layout = MainView.class)
@ViewController("MesRunliangdou.detail")
@ViewDescriptor("mes-runliangdou-detail-view.xml")
@EditedEntityContainer("mesRunliangdouDc")
public class MesRunliangdouDetailView extends StandardDetailView<MesRunliangdou> {
}
