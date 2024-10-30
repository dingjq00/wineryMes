package com.mom.winery.view.meszengguooperation;

import com.mom.winery.entity.MesZengguoOperation;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 16:16
 */

@Route(value = "mesZengguoOperations/:id", layout = MainView.class)
@ViewController("MesZengguoOperation.detail")
@ViewDescriptor("mes-zengguo-operation-detail-view.xml")
@EditedEntityContainer("mesZengguoOperationDc")
public class MesZengguoOperationDetailView extends StandardDetailView<MesZengguoOperation> {
}
