package com.mom.winery.view.messhifttime;

import com.mom.winery.entity.MesShiftTime;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/20 12:23
 */

@Route(value = "mesShiftTimes/:id", layout = MainView.class)
@ViewController(id = "MesShiftTime.detail")
@ViewDescriptor(path = "mes-shift-time-detail-view.xml")
@EditedEntityContainer("mesShiftTimeDc")
public class MesShiftTimeDetailView extends StandardDetailView<MesShiftTime> {
}
