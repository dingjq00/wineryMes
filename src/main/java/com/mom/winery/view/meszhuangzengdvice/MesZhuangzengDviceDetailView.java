package com.mom.winery.view.meszhuangzengdvice;

import com.mom.winery.entity.MesZhuangzengDvice;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:35
 */

@Route(value = "mesZhuangzengDvices/:id", layout = MainView.class)
@ViewController("MesZhuangzengDvice.detail")
@ViewDescriptor("mes-zhuangzeng-dvice-detail-view.xml")
@EditedEntityContainer("mesZhuangzengDviceDc")
public class MesZhuangzengDviceDetailView extends StandardDetailView<MesZhuangzengDvice> {
}
