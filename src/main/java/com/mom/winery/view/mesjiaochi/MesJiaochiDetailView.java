package com.mom.winery.view.mesjiaochi;

import com.mom.winery.entity.MesJiaochi;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:39
 */

@Route(value = "mesJiaochis/:id", layout = MainView.class)
@ViewController("MesJiaochi.detail")
@ViewDescriptor("mes-jiaochi-detail-view.xml")
@EditedEntityContainer("mesJiaochiDc")
public class MesJiaochiDetailView extends StandardDetailView<MesJiaochi> {
}
