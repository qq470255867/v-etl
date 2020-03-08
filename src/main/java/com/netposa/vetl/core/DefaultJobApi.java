package com.netposa.vetl.core;

import com.alibaba.fastjson.JSON;
import com.netposa.vetl.dao.JobEntityMapper;
import com.netposa.vetl.entity.JobEntity;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @program: vetl-quartz-server
 * @description: job core 操作api
 * @author: kangyu
 * @create: 2020-01-06 16:23
 **/
@Component
@Slf4j
public class DefaultJobApi extends AbstractJobApi{

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    JobEntityMapper jobEntityMapper;

    @Override
   public String refreshJob(JobEntity jobEntity) {

        String result;
        try {
            synchronized (log) {
                JobKey jobKey = getJobKey(jobEntity);
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                System.out.println(JSON.toJSONString(scheduler));
                scheduler.pauseJob(jobKey);
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                scheduler.deleteJob(jobKey);
                JobDataMap map = getJobDataMap(jobEntity);
                JobDetail jobDetail = getJobDetail(jobKey, jobEntity.getDescription(), map);
                if (jobEntity.isOpen()) {
                    scheduler.scheduleJob(jobDetail, getTrigger(jobEntity));
                    result = "Refresh Job : " + jobEntity.getName() + "\t jarPath: " + jobEntity.getJarPath() + " success !";
                } else {
                    result = "Refresh Job : " + jobEntity.getName() + "\t jarPath: " + jobEntity.getJarPath() + " failed ! , " +
                            "Because the Job status is " + jobEntity.isOpen();
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            result= e.getMessage();
        }
        return result;
    }

    @Override
    public String modifyJobCron(JobEntity jobEntity) {
        if (!CronExpression.isValidExpression(jobEntity.getCron())) return "cron is invalid !";
        synchronized (log) {
            if (jobEntity.isOpen()) {
                try {
                    JobKey jobKey = getJobKey(jobEntity);
                    TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());
                    Scheduler scheduler = schedulerFactoryBean.getScheduler();
                    CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    String oldCron = cronTrigger.getCronExpression();
                    if (!oldCron.equalsIgnoreCase(jobEntity.getCron())) {
                        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCron());
                        CronTrigger trigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobKey.getName(), jobKey.getGroup())
                                .withSchedule(cronScheduleBuilder)
                                .usingJobData(getJobDataMap(jobEntity))
                                .build();
                        scheduler.rescheduleJob(triggerKey, trigger);
                    }
                } catch (Exception e) {
                    log.error("printStackTrace", e);
                }
            } else {
                log.info("Job jump name : {} , Because {} status is {}", jobEntity.getName(), jobEntity.getName(), jobEntity.isOpen());
                return "modify failure , because the job is closed";
            }
        }
        return "modify success";
    }

    @Override
    public void deleteJob(JobEntity jobEntity) {
        try {
            synchronized (log) {
                JobKey jobKey = getJobKey(jobEntity);
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                scheduler.pauseJob(jobKey);
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                scheduler.deleteJob(jobKey);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
