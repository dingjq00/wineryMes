package com.mom.winery.view.supersetshiftteam;


import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/21 10:50
 */

@Route(value = "superset-shiftteam-view", layout = MainView.class)
@ViewController(id = "SupersetShiftteamView")
@ViewDescriptor(path = "superset-shiftteam-view.xml")
public class SupersetShiftteamView extends StandardView {
}
