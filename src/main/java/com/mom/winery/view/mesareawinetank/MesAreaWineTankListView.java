package com.mom.winery.view.mesareawinetank;

import com.mom.winery.entity.MesAreaWineTank;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:43
 */

@Route(value = "mesAreaWineTanks", layout = MainView.class)
@ViewController("MesAreaWineTank.list")
@ViewDescriptor("mes-area-wine-tank-list-view.xml")
@LookupComponent("mesAreaWineTanksDataGrid")
@DialogMode(width = "64em")
public class MesAreaWineTankListView extends StandardListView<MesAreaWineTank> {
}
