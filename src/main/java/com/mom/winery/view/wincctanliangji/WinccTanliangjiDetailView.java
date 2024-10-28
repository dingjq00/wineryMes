package com.mom.winery.view.wincctanliangji;

import com.mom.winery.entity.WinccTanliangji;
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

@Route(value = "winccTanliangjis/:id", layout = MainView.class)
@ViewController("WinccTanliangji.detail")
@ViewDescriptor("wincc-tanliangji-detail-view.xml")
@EditedEntityContainer("winccTanliangjiDc")
public class WinccTanliangjiDetailView extends StandardDetailView<WinccTanliangji> {
}
