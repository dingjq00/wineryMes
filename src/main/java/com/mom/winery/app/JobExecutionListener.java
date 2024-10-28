package com.mom.winery.app;

import org.springframework.stereotype.Component;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import javax.annotation.PostConstruct;
import jakarta.annotation.PostConstruct;

import java.util.Date;

@Component
public class JobExecutionListener extends JobListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobExecutionListener.class);

    @Autowired
    private Scheduler scheduler;

    @Override
    public String getName() {
        return "SampleJobExecutionListener";
    }

    @PostConstruct
    private void registerListener() {
        try {
            scheduler.getListenerManager().addJobListener(this);
        } catch (SchedulerException e) {
            log.error("Cannot register job listener", e);
        }
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("Job started: name={}, startTime={}",
                context.getJobDetail().getKey().getName(), context.getFireTime());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        long runTime = context.getJobRunTime();
        Date endTime = new Date(context.getFireTime().getTime() + runTime);
        log.info("Job ended: name={},duration={}, endTime={}",
                context.getJobDetail().getKey().getName(),runTime, endTime);
    }
}
