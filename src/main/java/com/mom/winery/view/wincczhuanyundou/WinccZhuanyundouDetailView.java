package com.mom.winery.view.wincczhuanyundou;

import com.mom.winery.entity.WinccZhuanyundou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:17
 */

@Route(value = "winccZhuanyundous/:id", layout = MainView.class)
@ViewController("WinccZhuanyundou.detail")
@ViewDescriptor("wincc-zhuanyundou-detail-view.xml")
@EditedEntityContainer("winccZhuanyundouDc")
public class WinccZhuanyundouDetailView extends StandardDetailView<WinccZhuanyundou> {
}
