package com.mom.winery.view.meszengguo;

import com.mom.winery.entity.MesZengguo;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:36
 */

@Route(value = "mesZengguos/:id", layout = MainView.class)
@ViewController("MesZengguo.detail")
@ViewDescriptor("mes-zengguo-detail-view.xml")
@EditedEntityContainer("mesZengguoDc")
public class MesZengguoDetailView extends StandardDetailView<MesZengguo> {
}
