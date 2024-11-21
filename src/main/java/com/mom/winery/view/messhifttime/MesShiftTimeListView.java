package com.mom.winery.view.messhifttime;

import com.mom.winery.entity.MesShiftTime;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/20 12:23
 */

@Route(value = "mesShiftTimes", layout = MainView.class)
@ViewController(id = "MesShiftTime.list")
@ViewDescriptor(path = "mes-shift-time-list-view.xml")
@LookupComponent("mesShiftTimesDataGrid")
@DialogMode(width = "64em")
public class MesShiftTimeListView extends StandardListView<MesShiftTime> {
}
