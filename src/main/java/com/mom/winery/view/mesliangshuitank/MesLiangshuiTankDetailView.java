package com.mom.winery.view.mesliangshuitank;

import com.mom.winery.entity.MesLiangshuiTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:39
 */

@Route(value = "mesLiangshuiTanks/:id", layout = MainView.class)
@ViewController("MesLiangshuiTank.detail")
@ViewDescriptor("mes-liangshui-tank-detail-view.xml")
@EditedEntityContainer("mesLiangshuiTankDc")
public class MesLiangshuiTankDetailView extends StandardDetailView<MesLiangshuiTank> {
}
