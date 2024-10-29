package com.mom.winery.view.meszengguorealdata;

import com.mom.winery.entity.MesZengguoRealData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/29 08:56
 */

@Route(value = "mesZengguoRealDatas/:id", layout = MainView.class)
@ViewController("MesZengguoRealData.detail")
@ViewDescriptor("mes-zengguo-real-data-detail-view.xml")
@EditedEntityContainer("mesZengguoRealDataDc")
public class MesZengguoRealDataDetailView extends StandardDetailView<MesZengguoRealData> {
}
