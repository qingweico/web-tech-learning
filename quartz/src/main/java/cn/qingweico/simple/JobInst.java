package cn.qingweico.simple;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * @author zqw
 * @date 2023/4/1
 */
public class JobInst implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext)  {
        System.out.println(new Date());
    }
}
