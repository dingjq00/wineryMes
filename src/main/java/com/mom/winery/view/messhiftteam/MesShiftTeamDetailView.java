package com.mom.winery.view.messhiftteam;

import com.mom.winery.entity.MesShiftTeam;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/19 11:35
 */

@Route(value = "mesShiftTeams/:id", layout = MainView.class)
@ViewController(id = "MesShiftTeam.detail")
@ViewDescriptor(path = "mes-shift-team-detail-view.xml")
@EditedEntityContainer("mesShiftTeamDc")
public class MesShiftTeamDetailView extends StandardDetailView<MesShiftTeam> {
}
