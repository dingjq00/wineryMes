package com.mom.winery.view.jobconfig;

import com.mom.winery.entity.JobConfig;
import com.mom.winery.entity.MesArea;
import com.mom.winery.view.main.MainView;
import com.mom.winery.view.mesarea.MesAreaListView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/26 14:33
 */

@Route(value = "jobConfigs", layout = MainView.class)
@ViewController("JobConfig.list")
@ViewDescriptor("job-config-list-view.xml")
@LookupComponent("jobConfigsDataGrid")
@DialogMode(width = "64em")
public class JobConfigListView extends StandardListView<JobConfig> {
    @ViewComponent
    private DataGrid<JobConfig> jobConfigsDataGrid;
    @Autowired
    private Notifications notifications;
    @Autowired
    private DataManager dataManager;

    @Subscribe(id = "createBasicInfo", subject = "clickListener")
    public void onCreateBasicInfoClick(final ClickEvent<JmixButton> event) {
        JobConfig rawJobConfig = jobConfigsDataGrid.getSingleSelectedItem();
        if(rawJobConfig == null) {
            notifications.create("Create Basic Info")
                    .show();
            return;
        }
        List<JobConfig> rawJobConfigs = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mesArea = :rawArea")
                .parameter("rawArea", rawJobConfig.getMesArea())
                .list();

        List<MesArea> newMesAreas = dataManager.load(MesArea.class)
                .query("select e from MesArea e where e.mesShopfloor.mesShopfloorCode = :shopFloorCode")
                .parameter("shopFloorCode", "13车间")
                .list();
        List<JobConfig> newJobConfigList = new ArrayList<>();
        for (MesArea newMesArea : newMesAreas) {
            for (JobConfig jobConfig : rawJobConfigs) {
                JobConfig newJobConfig = dataManager.create(JobConfig.class);
                newJobConfig.setMesArea(newMesArea);
                newJobConfig.setMainPhase(jobConfig.getMainPhase());
                newJobConfig.setWinccId(0);
                newJobConfigList.add(newJobConfig);
            }
        }
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(newJobConfigList));

    }
}
