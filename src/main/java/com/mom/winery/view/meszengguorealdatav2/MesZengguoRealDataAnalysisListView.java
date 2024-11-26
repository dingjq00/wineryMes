package com.mom.winery.view.meszengguorealdatav2;

import com.mom.winery.entity.MesZengguo;
import com.mom.winery.entity.MesZengguoRealDataV2;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;

import java.util.Date;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/26 19:07
 */

@Route(value = "mesZengguoRealDataAnalysis", layout = MainView.class)
@ViewController(id = "MesZengguoRealDataAnalysis.list")
@ViewDescriptor(path = "mes-zengguo-real-data-analysis-list-view.xml")
@LookupComponent("mesZengguoRealDataV2sDataGrid")
@DialogMode(width = "100%", height = "100%")
public class MesZengguoRealDataAnalysisListView extends StandardListView<MesZengguoRealDataV2> {
    private MesZengguo mesZengguo;
    private Date startTime;
    private Date endTime;
    @ViewComponent
    private CollectionLoader<MesZengguoRealDataV2> mesZengguoRealDataV2sDl;

    public void setMesZengguo(MesZengguo mesZengguo) {
        this.mesZengguo = mesZengguo;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        mesZengguoRealDataV2sDl.setParameter("mesZengguo", mesZengguo);
        mesZengguoRealDataV2sDl.setParameter("startTime", startTime);
        mesZengguoRealDataV2sDl.setParameter("endTime", endTime);
        mesZengguoRealDataV2sDl.load();
    }






}
