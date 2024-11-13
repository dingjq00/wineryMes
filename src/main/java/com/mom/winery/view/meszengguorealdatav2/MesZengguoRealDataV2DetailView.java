package com.mom.winery.view.meszengguorealdatav2;

import com.mom.winery.entity.MesZengguoRealDataV2;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/1 16:42
 */

@Route(value = "mesZengguoRealDataV2s/:id", layout = MainView.class)
@ViewController("MesZengguoRealDataV2.detail")
@ViewDescriptor("mes-zengguo-real-data-v2-detail-view.xml")
@EditedEntityContainer("mesZengguoRealDataV2Dc")
public class MesZengguoRealDataV2DetailView extends StandardDetailView<MesZengguoRealDataV2> {
}
