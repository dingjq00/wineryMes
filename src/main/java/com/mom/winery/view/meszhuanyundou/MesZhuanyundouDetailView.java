package com.mom.winery.view.meszhuanyundou;

import com.mom.winery.entity.MesZhuanyundou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:34
 */

@Route(value = "mesZhuanyundous/:id", layout = MainView.class)
@ViewController("MesZhuanyundou.detail")
@ViewDescriptor("mes-zhuanyundou-detail-view.xml")
@EditedEntityContainer("mesZhuanyundouDc")
public class MesZhuanyundouDetailView extends StandardDetailView<MesZhuanyundou> {
}
