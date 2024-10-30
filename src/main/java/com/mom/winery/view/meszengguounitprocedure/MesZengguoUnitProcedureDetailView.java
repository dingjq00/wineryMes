package com.mom.winery.view.meszengguounitprocedure;

import com.mom.winery.entity.MesZengguoUnitProcedure;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 18:13
 */

@Route(value = "mesZengguoUnitProcedures/:id", layout = MainView.class)
@ViewController("MesZengguoUnitProcedure.detail")
@ViewDescriptor("mes-zengguo-unit-procedure-detail-view.xml")
@EditedEntityContainer("mesZengguoUnitProcedureDc")
public class MesZengguoUnitProcedureDetailView extends StandardDetailView<MesZengguoUnitProcedure> {
}
