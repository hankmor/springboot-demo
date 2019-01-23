package com.belonk.quartz.service;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

/**
 * Created by sun on 2019/1/11.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public interface SchedulerService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constants/Initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interfaces
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Schedule the {@code jobDetail} at the {@code trigger}.
     *
     * @param jobDetail the job to schedule
     * @param trigger   when trigger the scheduling
     */
    void schedule(JobDetail jobDetail, Trigger trigger);

    /**
     * Reschedule a trigger whose key is {@code triggerKey} at the {@code newTrigger}.
     *
     * @param triggerKey trigger key
     * @param newTrigger new trigger to reschedule
     */
    void reschedule(TriggerKey triggerKey, Trigger newTrigger);

    /**
     * Unschedule a trigger whose key is {@code triggerKey}.
     *
     * @param triggerKey the trigger key to be destroyed
     */
    void unscheduleJob(TriggerKey triggerKey);

    /**
     * Get job detail by {@code jobKey}.
     *
     * @param jobKey job key
     * @return job detail object
     */
    JobDetail getJobDetail(JobKey jobKey);
}
