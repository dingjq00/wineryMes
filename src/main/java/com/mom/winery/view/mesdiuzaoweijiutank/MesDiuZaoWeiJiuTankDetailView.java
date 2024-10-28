package com.mom.winery.view.mesdiuzaoweijiutank;

import com.mom.winery.entity.MesDiuZaoWeiJiuTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:47
 */

@Route(value = "mesDiuZaoWeiJiuTanks/:id", layout = MainView.class)
@ViewController("MesDiuZaoWeiJiuTank.detail")
@ViewDescriptor("mes-diu-zao-wei-jiu-tank-detail-view.xml")
@EditedEntityContainer("mesDiuZaoWeiJiuTankDc")
public class MesDiuZaoWeiJiuTankDetailView extends StandardDetailView<MesDiuZaoWeiJiuTank> {
}
