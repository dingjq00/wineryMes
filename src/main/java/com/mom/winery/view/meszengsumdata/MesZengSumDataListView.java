package com.mom.winery.view.meszengsumdata;

import com.mom.winery.entity.MesZengSumData;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/13 12:38
 */

@Route(value = "mesZengSumDatas", layout = MainView.class)
@ViewController(id = "MesZengSumData.list")
@ViewDescriptor(path = "mes-zeng-sum-data-list-view.xml")
@LookupComponent("mesZengSumDatasDataGrid")
@DialogMode(width = "64em")
public class MesZengSumDataListView extends StandardListView<MesZengSumData> {
}
