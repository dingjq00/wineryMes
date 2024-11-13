package com.mom.winery.view.supersetsummary;


import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/3 11:51
 */

@Route(value = "superset-summary-view", layout = MainView.class)
@ViewController("SupersetSummaryView")
@ViewDescriptor("superset-summary-view.xml")
public class SupersetSummaryView extends StandardView {
}
