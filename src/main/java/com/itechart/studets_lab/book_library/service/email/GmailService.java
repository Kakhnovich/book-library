package com.itechart.studets_lab.book_library.service.email;

import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class GmailService {
    private static final Logger LOGGER = LogManager.getLogger(GmailService.class);
    private static final GmailService INSTANCE = new GmailService();
    private static final String NAME_OF_JOB = "sendGmail";
    private static final String NAME_OF_GROUP = "gmailGroup";
    private static final String NAME_OF_TRIGGER = "triggerStart";
    private static final int TIME_INTERVAL = 24;
    private Scheduler sched;

    private GmailService() {
    }

    public static GmailService getInstance() {
        return INSTANCE;
    }

    public void startScheduler() {
        try {
            BasicConfigurator.configure();
            sched = new StdSchedulerFactory().getScheduler();
            sched.start();
            JobDetail jobInstance = JobBuilder.newJob(GmailSender.class).withIdentity(NAME_OF_JOB, NAME_OF_GROUP).build();
            Trigger triggerNew = TriggerBuilder.newTrigger().withIdentity(NAME_OF_TRIGGER, NAME_OF_GROUP)
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(TIME_INTERVAL).repeatForever())
                    .build();
            sched.scheduleJob(jobInstance, triggerNew);
        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException while trying to start schedule " + e.getLocalizedMessage());
        }
    }

    public void shutdownScheduler() {
        try {
            sched.shutdown();
        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException while trying to shutdown schedule " + e.getLocalizedMessage());
        }
    }
}
