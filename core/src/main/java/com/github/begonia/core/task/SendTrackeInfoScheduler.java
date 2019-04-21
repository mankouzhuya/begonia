package com.github.begonia.core.task;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Arrays;

public class SendTrackeInfoScheduler {

    public void fire()  throws SchedulerException, InterruptedException {
        //jobDetail
        JobDetail jobDetail = JobBuilder.newJob(SendTrackeInfoJob.class).withIdentity("cronJob").build();
        //cronTrigger
        //每日的9点40触发任务
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")).build();////10分钟一次
        //1.每日10点15分触发      0 15 10 ？* *
        //2.每天下午的2点到2点59分（正点开始，隔5分触发）       0 0/5 14 * * ?
        //3.从周一到周五每天的上午10点15触发      0 15 10 ? MON-FRI
        //4.每月的第三周的星期五上午10点15触发     0 15 10 ? * 6#3
        //5.2016到2017年每月最后一周的星期五的10点15分触发   0 15 10 ? * 6L 2016-2017
        //Scheduler实例
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail,cronTrigger);
    }

}
