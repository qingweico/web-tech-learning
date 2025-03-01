package cn.qingweico.delayTask;

import cn.qingweico.client.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;

/**
 * 基于Zset的延迟队列
 *
 * @author zqw
 * @date 2025-02-27
 */
@Slf4j
@Component
public class ZSetDelayQueue {
    @Resource
    private RedisClient redisClient;
    private final static String KEY = "ZSet_delay";


    public void addTask(String taskId, long delayMs) {
        long executeTime = Instant.now().toEpochMilli() + delayMs;
        redisClient.zadd(KEY, executeTime, taskId);
        log.info("任务 {} 已加入延迟队列，执行时间: {}", taskId, executeTime);
    }


    public void pollTasks() {
        long now = Instant.now().toEpochMilli();
        List<String> taskIds = redisClient.zrangebyscore(KEY, 0, now);

        if (taskIds.isEmpty()) {
            log.info("当前无到期任务");
            return;
        }

        for (String taskId : taskIds) {
            Long removed = redisClient.zrem(KEY, taskId);
            if (removed != null && removed > 0) {
                log.info("执行任务: {}", taskId);
            }
        }
    }

}
