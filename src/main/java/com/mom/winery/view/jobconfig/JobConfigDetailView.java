package com.mom.winery.view.jobconfig;

import com.mom.winery.entity.JobConfig;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/26 14:33
 */

@Route(value = "jobConfigs/:id", layout = MainView.class)
@ViewController("JobConfig.detail")
@ViewDescriptor("job-config-detail-view.xml")
@EditedEntityContainer("jobConfigDc")
public class JobConfigDetailView extends StandardDetailView<JobConfig> {
}
