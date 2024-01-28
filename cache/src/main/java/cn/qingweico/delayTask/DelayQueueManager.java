package cn.qingweico.delayTask;

import cn.qingweico.client.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;


/**
 * @author zqw
 * @date 2023/11/19
 */
@Component
@Slf4j
public class DelayQueueManager implements CommandLineRunner {
    private final DelayQueue<DelayTask> delayQueue = new DelayQueue<>();

    @Resource
    private RedisClient redisClient;

    /**
     * 加入到延时队列中
     *
     * @param task DelayTask
     */
    public void put(DelayTask task) {
        log.info("加入延时任务 : {}", task);
        delayQueue.put(task);
    }

    /**
     * 取消延时任务
     *
     * @param task DelayTask
     * @return boolean
     */
    public boolean remove(DelayTask task) {
        log.info("取消延时任务 : {}", task);
        return delayQueue.remove(task);
    }

    /**
     * 取消延时任务
     *
     * @param taskId 任务ID
     * @return boolean
     */
    public boolean remove(String taskId) {
        return remove(new DelayTask(taskId, 0));
    }

    @Override
    public void run(String... args) {
        log.info("初始化日志延时队列");
        Runnable r = this::executeThread;
        CompletableFuture.runAsync(r);
    }

    /**
     * 延时任务执行线程
     */
    private void executeThread() {
        int i = 0;
        boolean b = true;
        while (b) {
            try {
                DelayTask task = delayQueue.take();
                i = i + processTask(task);
                b = (i < Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                log.error("executeThread : {}", e.getMessage());
                break;
            }
        }
    }

    /**
     * 内部执行延时任务
     *
     * @param task DelayTask
     */
    private int processTask(DelayTask task) {
        log.info("执行延时任务 : {}", task);
        if (task.getData() instanceof RedisBroadcastDelayTask) {
            RedisBroadcastDelayTask t = (RedisBroadcastDelayTask) task.getData();
            redisClient.publish(t.getChannel(), t.getMessage());
            return 1;
        }
        return 0;
    }
}
