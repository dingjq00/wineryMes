package com.mom.winery.view.meszengguooperation;

import com.mom.winery.entity.MesZengguoOperation;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 16:16
 */

@Route(value = "mesZengguoOperations", layout = MainView.class)
@ViewController("MesZengguoOperation.list")
@ViewDescriptor("mes-zengguo-operation-list-view.xml")
@LookupComponent("mesZengguoOperationsDataGrid")
@DialogMode(width = "64em")
public class MesZengguoOperationListView extends StandardListView<MesZengguoOperation> {
}
