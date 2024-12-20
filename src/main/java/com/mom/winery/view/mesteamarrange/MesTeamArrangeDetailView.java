package com.mom.winery.view.mesteamarrange;

import com.mom.winery.entity.MesTeamArrange;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/20 12:24
 */

@Route(value = "mesTeamArranges/:id", layout = MainView.class)
@ViewController(id = "MesTeamArrange.detail")
@ViewDescriptor(path = "mes-team-arrange-detail-view.xml")
@EditedEntityContainer("mesTeamArrangeDc")
public class MesTeamArrangeDetailView extends StandardDetailView<MesTeamArrange> {
}
