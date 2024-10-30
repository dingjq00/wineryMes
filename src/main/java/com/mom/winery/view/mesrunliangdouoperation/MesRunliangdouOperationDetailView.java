package com.mom.winery.view.mesrunliangdouoperation;

import com.mom.winery.entity.MesRunliangdouOperation;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 11:33
 */

@Route(value = "mesRunliangdouOperations/:id", layout = MainView.class)
@ViewController("MesRunliangdouOperation.detail")
@ViewDescriptor("mes-runliangdou-operation-detail-view.xml")
@EditedEntityContainer("mesRunliangdouOperationDc")
public class MesRunliangdouOperationDetailView extends StandardDetailView<MesRunliangdouOperation> {
}
