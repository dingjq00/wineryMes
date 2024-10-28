package com.mom.winery.view.meshangche;

import com.mom.winery.entity.MesHangche;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:57
 */

@Route(value = "mesHangches/:id", layout = MainView.class)
@ViewController("MesHangche.detail")
@ViewDescriptor("mes-hangche-detail-view.xml")
@EditedEntityContainer("mesHangcheDc")
public class MesHangcheDetailView extends StandardDetailView<MesHangche> {
}
