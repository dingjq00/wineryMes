package com.mom.winery.view.mesdiuzaoweijiutank;

import com.mom.winery.entity.MesDiuZaoWeiJiuTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 18:47
 */

@Route(value = "mesDiuZaoWeiJiuTanks", layout = MainView.class)
@ViewController("MesDiuZaoWeiJiuTank.list")
@ViewDescriptor("mes-diu-zao-wei-jiu-tank-list-view.xml")
@LookupComponent("mesDiuZaoWeiJiuTanksDataGrid")
@DialogMode(width = "64em")
public class MesDiuZaoWeiJiuTankListView extends StandardListView<MesDiuZaoWeiJiuTank> {
}
