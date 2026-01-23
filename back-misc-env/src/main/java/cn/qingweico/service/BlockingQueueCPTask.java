package cn.qingweico.service;

import jodd.util.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 使用 Redisson {@link RBlockingQueue} 处理生产者消费者模型任务
 *
 * @author zqw
 * @date 2025/11/30
 */
@Slf4j
@Component
public class BlockingQueueCPTask {
    private final RedissonClient redissonClient;
    private final ExecutorService pool;

    @SuppressWarnings("FieldCanBeLocal")
    private final int MAX_TASK_HANDLE_COUNT = 6;
    ConcurrentLinkedQueue<Task> tasks = new ConcurrentLinkedQueue<>();

    public BlockingQueueCPTask(RedissonClient redissonClient,
                               ExecutorService pool) {
        this.redissonClient = redissonClient;
        this.pool = pool;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 20; i++) {
            tasks.add(new Task(RandomStringUtils.randomAlphanumeric(10)));
        }
    }

    private void supplier() {
        RBlockingQueue<Task> bq = redissonClient.getBlockingQueue("consumer-producer-task");
        while (!tasks.isEmpty()) {
            if (bq.size() < MAX_TASK_HANDLE_COUNT) {
                log.info("Task 任务数量少于 {}, 开始添加任务", MAX_TASK_HANDLE_COUNT);
                bq.add(tasks.remove());
            }
        }
        log.info("Task 任务已全部发送到队列....");
    }

    private void doRequest() {
        RBlockingQueue<Task> bq = redissonClient.getBlockingQueue("consumer-producer-task");
        CountDownLatch tl = new CountDownLatch(tasks.size());
        while (!tasks.isEmpty() || !bq.isEmpty()) {
            try {
                while (!bq.isEmpty()) {
                    bq.take();
                    pool.execute(() -> {
                        try {
                            log.info("从任务队列中拿到任务, 开始处理...");
                            ThreadUtil.sleep(4500);
                        } finally {
                            tl.countDown();
                        }
                    });
                }
                ThreadUtil.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        try {
            tl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("本批次任务已全部处理完毕..... 暂无可处理的任务");
        pool.shutdown();
        // 调用 pool.awaitTermination 时, 不要和外层共用一个线程池, 因为
        // 其本身是作为一个任务提交到线程池中, 导致线程池中永远有一个任务在执行
        // 直到时间超时或者被打断才可以正常结束, 不然一直阻塞
        // pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    public void start() {
        RBlockingQueue<Task> bq = redissonClient.getBlockingQueue("consumer-producer-task");
        bq.clear();
        CountDownLatch latch = new CountDownLatch(2);
        pool.execute(() -> {
            supplier();
            latch.countDown();
        });
        pool.execute(() -> {
            doRequest();
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Task {
        private String id;
    }
}
