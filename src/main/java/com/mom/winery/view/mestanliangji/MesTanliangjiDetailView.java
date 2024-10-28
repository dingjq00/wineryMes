package com.mom.winery.view.mestanliangji;

import com.mom.winery.entity.MesTanliangji;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:37
 */

@Route(value = "mesTanliangjis/:id", layout = MainView.class)
@ViewController("MesTanliangji.detail")
@ViewDescriptor("mes-tanliangji-detail-view.xml")
@EditedEntityContainer("mesTanliangjiDc")
public class MesTanliangjiDetailView extends StandardDetailView<MesTanliangji> {
}
