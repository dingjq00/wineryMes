package com.mom.winery.view.meszengguounitprocedure;

import com.mom.winery.entity.MesZengguoUnitProcedure;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 18:13
 */

@Route(value = "mesZengguoUnitProcedures", layout = MainView.class)
@ViewController("MesZengguoUnitProcedure.list")
@ViewDescriptor("mes-zengguo-unit-procedure-list-view.xml")
@LookupComponent("mesZengguoUnitProceduresDataGrid")
@DialogMode(width = "64em")
public class MesZengguoUnitProcedureListView extends StandardListView<MesZengguoUnitProcedure> {
}
