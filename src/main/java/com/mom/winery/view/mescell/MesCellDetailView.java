package com.mom.winery.view.mescell;

import com.mom.winery.entity.MesCell;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:42
 */

@Route(value = "mesCells/:id", layout = MainView.class)
@ViewController("MesCell.detail")
@ViewDescriptor("mes-cell-detail-view.xml")
@EditedEntityContainer("mesCellDc")
public class MesCellDetailView extends StandardDetailView<MesCell> {
}
