package cn.qingweico.simple;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Elastic-job | XX-job
 *
 * @author zqw
 * @date 2023/4/1
 */
public class SimpleScheduleApplication {
    public static void main(String[] args) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail = JobBuilder.newJob(JobInst.class)
                .withIdentity("job-01", "Group")
                .build();

        Trigger tb = TriggerBuilder.newTrigger().
                withIdentity("trigger-01", "Group")
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                .build();

        // 将触发器和任务实例关联
        scheduler.scheduleJob(jobDetail, tb);

        // 启动调度器
        scheduler.start();

    }
}
