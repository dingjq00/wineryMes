package com.mom.winery.view.mesrunliangdouoperation;

import com.mom.winery.entity.MesRunliangdouOperation;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 11:33
 */

@Route(value = "mesRunliangdouOperations", layout = MainView.class)
@ViewController("MesRunliangdouOperation.list")
@ViewDescriptor("mes-runliangdou-operation-list-view.xml")
@LookupComponent("mesRunliangdouOperationsDataGrid")
@DialogMode(width = "64em")
public class MesRunliangdouOperationListView extends StandardListView<MesRunliangdouOperation> {
}
