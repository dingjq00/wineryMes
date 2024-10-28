package com.mom.winery.view.wincchangche;

import com.mom.winery.entity.WinccHangche;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 10:56
 */

@Route(value = "winccHangches/:id", layout = MainView.class)
@ViewController("WinccHangche.detail")
@ViewDescriptor("wincc-hangche-detail-view.xml")
@EditedEntityContainer("winccHangcheDc")
public class WinccHangcheDetailView extends StandardDetailView<WinccHangche> {
}
