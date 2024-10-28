package com.mom.winery.view.mesliangshuitank;

import com.mom.winery.entity.MesLiangshuiTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:39
 */

@Route(value = "mesLiangshuiTanks", layout = MainView.class)
@ViewController("MesLiangshuiTank.list")
@ViewDescriptor("mes-liangshui-tank-list-view.xml")
@LookupComponent("mesLiangshuiTanksDataGrid")
@DialogMode(width = "64em")
public class MesLiangshuiTankListView extends StandardListView<MesLiangshuiTank> {
}
