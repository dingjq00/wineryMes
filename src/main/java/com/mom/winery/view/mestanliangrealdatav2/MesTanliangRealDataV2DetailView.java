package com.mom.winery.view.mestanliangrealdatav2;

import com.mom.winery.entity.MesTanliangRealDataV2;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/1 16:41
 */

@Route(value = "mesTanliangRealDataV2s/:id", layout = MainView.class)
@ViewController("MesTanliangRealDataV2.detail")
@ViewDescriptor("mes-tanliang-real-data-v2-detail-view.xml")
@EditedEntityContainer("mesTanliangRealDataV2Dc")
public class MesTanliangRealDataV2DetailView extends StandardDetailView<MesTanliangRealDataV2> {
}
