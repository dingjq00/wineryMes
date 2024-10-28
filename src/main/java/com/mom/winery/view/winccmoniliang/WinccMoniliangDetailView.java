package com.mom.winery.view.winccmoniliang;

import com.mom.winery.entity.WinccMoniliang;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 11:10
 */

@Route(value = "winccMoniliangs/:id", layout = MainView.class)
@ViewController("WinccMoniliang.detail")
@ViewDescriptor("wincc-moniliang-detail-view.xml")
@EditedEntityContainer("winccMoniliangDc")
public class WinccMoniliangDetailView extends StandardDetailView<WinccMoniliang> {
}
