package com.mom.winery.view.messhiftteam;

import com.mom.winery.entity.MesShiftTeam;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/19 11:35
 */

@Route(value = "mesShiftTeams", layout = MainView.class)
@ViewController(id = "MesShiftTeam.list")
@ViewDescriptor(path = "mes-shift-team-list-view.xml")
@LookupComponent("mesShiftTeamsDataGrid")
@DialogMode(width = "64em")
public class MesShiftTeamListView extends StandardListView<MesShiftTeam> {
}
