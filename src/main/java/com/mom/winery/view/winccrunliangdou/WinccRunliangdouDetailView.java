package com.mom.winery.view.winccrunliangdou;

import com.mom.winery.entity.WinccRunliangdou;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:11
 */

@Route(value = "winccRunliangdous/:id", layout = MainView.class)
@ViewController("WinccRunliangdou.detail")
@ViewDescriptor("wincc-runliangdou-detail-view.xml")
@EditedEntityContainer("winccRunliangdouDc")
public class WinccRunliangdouDetailView extends StandardDetailView<WinccRunliangdou> {
}
