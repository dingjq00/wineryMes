package com.mom.winery.view.mesglobalrealdata;

import com.mom.winery.entity.MesGlobalRealData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/29 23:10
 */

@Route(value = "mesGlobalRealDatas/:id", layout = MainView.class)
@ViewController("MesGlobalRealData.detail")
@ViewDescriptor("mes-global-real-data-detail-view.xml")
@EditedEntityContainer("mesGlobalRealDataDc")
public class MesGlobalRealDataDetailView extends StandardDetailView<MesGlobalRealData> {
}
