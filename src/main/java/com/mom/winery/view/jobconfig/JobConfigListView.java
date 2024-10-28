package com.mom.winery.view.jobconfig;

import com.mom.winery.entity.JobConfig;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


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
}
