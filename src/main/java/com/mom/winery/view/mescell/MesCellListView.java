package com.mom.winery.view.mescell;

import com.mom.winery.entity.MesCell;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:42
 */

@Route(value = "mesCells", layout = MainView.class)
@ViewController("MesCell.list")
@ViewDescriptor("mes-cell-list-view.xml")
@LookupComponent("mesCellsDataGrid")
@DialogMode(width = "64em")
public class MesCellListView extends StandardListView<MesCell> {
}
