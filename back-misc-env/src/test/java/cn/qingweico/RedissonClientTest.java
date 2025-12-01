package cn.qingweico;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.qingweico.convert.TimeUnitConverter;
import cn.qingweico.service.BillService;
import cn.qingweico.service.BlockingQueueCPTask;
import cn.qingweico.service.impl.BillServiceImpl;
import jodd.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.geo.GeoSearchParams;
import org.redisson.api.geo.ShapeGeoSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author zqw
 * @date 2025/11/30
 */
@Slf4j
@SpringBootTest(classes = BackMiscEnvApplication.class)
public class RedissonClientTest {
    @Resource
    private ExecutorService pool;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    private BillServiceImpl billServiceImpl;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private BlockingQueueCPTask blockingQueueCPTask;
    private int shared = 0;

    @Test
    public void redisTemplate() {
        redissonClient.getBucket("ttl").set(System.currentTimeMillis());
        redisTemplate.execute(new SessionCallback<>() {
            @NotNull
            @Override
            public Object execute(@NotNull RedisOperations redisOperations) throws DataAccessException {
                // 原始值
                System.out.println(redisOperations.opsForValue().get("ttl"));
                redisOperations.multi();
                // null 事务开始后和事务提交前都看不到事务的中间结果
                // 在 multi 和 exec 之间, 任何对 Redis 的读取操作都不会看到事务的中间结果
                System.out.println(redisOperations.opsForValue().get("ttl"));
                ThreadUtil.sleep(3000);
                // 取消事务, 清空所有排队的命令
                // redisOperations.discard();
                // 只有在调用 exec 时, 事务中的所有命令才会按顺序执行
                return redisOperations.exec();
            }
        });
    }

    @Test
    public void redissonStr() {
        String key = DateUtil.now() + "_Str";
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(RandomStringUtils.randomAlphanumeric(12));
        System.out.println(bucket.get());
    }

    @Test
    public void redissonList() {
        String key = DateUtil.now() + "_List";
        RList<Object> list = redissonClient.getList(key);
        ArrayList<String> coll = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> coll.add(RandomStringUtils.randomAlphanumeric(12)));
        list.addAll(coll);
        System.out.println(redissonClient.getList(key));
    }

    @Test
    public void keyPrefix() {
        String key = DateFormatUtils.format(new Date(), DatePattern.NORM_DATE_PATTERN) + "*";
        RKeys keys = redissonClient.getKeys();
        Iterable<String> keysByPattern = keys.getKeysByPattern(key);
        keysByPattern.forEach(System.out::println);
    }

    @Test
    public void redissonSet() {
        String key = DateUtil.now() + "_Set";
        RSet<Object> set = redissonClient.getSet(key);
        ArrayList<String> coll = new ArrayList<>();
        coll.add("1");
        coll.add("1");
        coll.add("1");
        set.addAll(coll);
        System.out.println(redissonClient.getSet(key));
    }

    @Test
    public void redissonMap() {
        String key = DateUtil.now() + "_Map";
        RMap<Object, Object> map = redissonClient.getMap(key);
        IntStream.range(0, 5).forEach(i ->
                map.put(UUID.randomUUID().toString(), RandomStringUtils.randomAlphanumeric(12)));
        System.out.println(redissonClient.getMap(key).readAllMap());
    }

    @Test
    public void redissonLock() throws InterruptedException {
        RLock lock = redissonClient.getSpinLock("redisson-lock");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            pool.execute(() -> {
                boolean acquired = false;
                try {
                    acquired = lock.tryLock(1000, TimeUnit.MILLISECONDS);
                    if (!acquired) {
                        throw new InterruptedException("系统繁忙");
                    }
                    for (int j = 0; j < 10; j++) {
                        shared = shared + 1;
                    }
                } catch (InterruptedException e) {
                    log.error("{}", e.getMessage());
                } finally {
                    if (acquired) {
                        lock.unlock();
                    }
                }
                latch.countDown();
            });
        }
        latch.await();
        stopWatch.stop();
        System.out.println(shared);
        System.out.println(TimeUnitConverter.convertMills(stopWatch.getTime()));
    }

    @Test
    public void threadPoolRejectedExecutionExplicit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            pool.execute(() -> {
                cn.hutool.core.thread.ThreadUtil.safeSleep(100);
                // 抛出拒绝策略 由于队列任务积压
                synchronized (this) {
                    for (int j = 0; j < 10; j++) {
                        shared = shared + 1;
                    }
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(shared);
    }

    @Test
    public void idGenerator() throws InterruptedException {
        int task = 100;
        long loop = 10000L;
        CountDownLatch latch = new CountDownLatch(task);
        // 或者使用google限流器
        // RateLimiter rateLimiter = RateLimiter.create(10000.0);
        RRateLimiter rate = redissonClient.getRateLimiter("rate-limiter");
        rate.setRate(RateType.PER_CLIENT, 100000, 1000, RateIntervalUnit.MILLISECONDS);
        RIdGenerator idGenerator = redissonClient.getIdGenerator("id-generator");
        ConcurrentHashSet<Long> set = new ConcurrentHashSet<>();
        for (int i = 0; i < task; i++) {
            pool.execute(() -> {
                // cn.hutool.core.thread.ThreadUtil.safeSleep(500);
                try {
                    for (int j = 0; j < loop; j++) {
                        // rateLimiter.acquire();
                        rate.acquire();
                        long l = idGenerator.nextId();
                        set.add(l);
                        // 注释掉下面打印就报错 StackOverflowError, RedissonIdGenerator.handleIdRequests
                        // 导致的起因: 循环速度快, 大量请求导致redisson队列背压问题(Backpressure)
                        // 解决原因: 打印实际起到了限流的作用, 控制台输出是比较慢的IO(同步阻塞)操作,显著降低了循环速度
                        // System.out.println(set.size());
                    }
                    System.out.println(set.size());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        pool.shutdown();
        Assertions.assertEquals(task * loop, set.size());
    }

    @Test
    public void remoteService() {
        RRemoteService remoteService = redissonClient.getRemoteService();
        remoteService.register(BillService.class, billServiceImpl);
    }

    @Test
    public void ringBuffer() {
        RRingBuffer<Object> ringBuffer = redissonClient.getRingBuffer("ring-buffer");
        ringBuffer.clear();
        System.out.println(ringBuffer.trySetCapacity(10));
        // ringBuffer.setCapacity(2);
        System.out.println(ringBuffer.capacity());
        IntStream.range(0, 19)
                .forEach(ringBuffer::add);
        System.out.println(ringBuffer.remainingCapacity());
        System.out.println(ringBuffer);
    }

    @Test
    public void getBuckets() {
        Map<String, String> map = new LinkedHashMap<>();
        IntStream.range(0, 10)
                .forEach(i -> map.put("Buckets" + i, RandomStringUtils.randomAlphanumeric(16)));
        redissonClient.getBuckets().set(map);
        System.out.println(redissonClient.getBuckets().get(map.keySet().toArray(new String[0])));
    }

    @Test
    public void atomicLong() throws InterruptedException {
        RAtomicLong along = redissonClient.getAtomicLong("atomic-long");
        along.set(0L);
        int task = 100;
        long loop = 10000L;
        CountDownLatch latch = new CountDownLatch(task);
        for (int i = 0; i < task; i++) {
            pool.execute(() -> {
                try {
                    for (int j = 0; j < loop; j++) {
                        along.getAndIncrement();
                    }
                    System.out.println(along.get());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        System.out.println(along.get());
    }

    /*版本支持: GEO 命令需要 Redis 3.2.0+*/
    @Test
    public void geo() {
        RGeo<Object> geo = redissonClient.getGeo("geo");
        // 经度(longitude)范围是 -180 到 180, 纬度(latitude)范围是 -90 到 90(-85.05112878 ~ 85.05112878)
        geo.add(new GeoEntry(116.391, 39.904, "北京"));
        geo.add(new GeoEntry(121.474, 31.230, "上海"));
        geo.add(new GeoEntry(113.264, 23.129, "广州"));
        geo.add(new GeoEntry(114.058, 22.543, "深圳"));


        geo.add(new GeoEntry(108.939, 34.342, "西安"));
        geo.add(new GeoEntry(104.066, 30.572, "成都"));
        geo.add(new GeoEntry(114.298, 30.584, "武汉"));

        Double dist = geo.dist("北京", "上海", GeoUnit.KILOMETERS);
        System.out.println(dist);

        Map<Object, GeoPosition> position = geo.pos("北京", "上海");
        MapUtils.verbosePrint(System.out, null, position);
        // 搜索特定地点附近的其他地点
        ShapeGeoSearch from = GeoSearchArgs.from("北京");
        // 搜索的半径为 2000 公里
        from.radius(1500, GeoUnit.KILOMETERS);
        List<Object> search = geo.search((GeoSearchParams) from);
        System.out.println(search);
    }

    @Test
    public void hyperLogLog() {
        // 一种概率数据结构, 用于估计集合中唯一元素的数量
        // 通过牺牲一定的准确性来大幅减少内存使用(占用的内存非常少),适合处理大规模数据集
        // 使用场景: 唯一访客统计/数据去重/实时监控
        RHyperLogLog<Object> hyperLogLog = redissonClient.getHyperLogLog("hyper-log-log");
        hyperLogLog.expire(Duration.ofDays(1));
        Random random = new Random();
        Set<Integer> set = new ConcurrentHashSet<>();
        IntStream.range(0, 1000)
                .forEach(i -> {
                    int r = random.nextInt(1000000);
                    set.add(r);
                    hyperLogLog.add(r);
                });
        long hll = hyperLogLog.count();
        System.out.println("RHyperLogLog count: " + hll);
        System.out.println("Set count: " + set.size());
    }

    /**
     * {@link RBucket#expire(Instant)} 和  {@link RBucket#expire(Duration)} 的区别
     * 两者的时间参考系不同, {@link RBucket#expire(Duration)} 使用相对时间, 从当前时刻开始结算,
     * 使用场景为缓存, 会话, 临时数据(即大部分缓存场景), 代码比较直观(xxx时间后过期)
     * {@link RBucket#expire(Instant)} 固定时间点(某一时刻), 适用定时任务, 固定时间点截至的场景
     * 代码可能不太直观, 未来的某一个时刻
     */
    @Test
    public void expireDiff() {
        RBucket<Object> bucket = redissonClient.getBucket("after-5-second-expire-key");
        // 推荐做法 保证原子性
        bucket.set(System.currentTimeMillis(), Duration.of(5, ChronoUnit.SECONDS));
        bucket.set(System.currentTimeMillis());
        bucket.expire(Duration.of(5, ChronoUnit.SECONDS));
        // bucket.expire(Instant.now().plusSeconds(5));
        System.out.println(bucket.get());
        ThreadUtil.sleep(5100);
        System.out.println(bucket.get());
        System.out.println(bucket.remainTimeToLive());
    }

    @Test
    public void delKey() {
        RBucket<Object> bucket = redissonClient.getBucket("transfer-queue");
        System.out.println(bucket.delete());
    }

    @Test
    public void timeSeries() {
        RTimeSeries<Object, Object> timeSeries = redissonClient.getTimeSeries("time-series");
        timeSeries.delete();
        timeSeries.add(Duration.ofMillis(System.currentTimeMillis()).minusDays(2).toMillis(), "前天");
        timeSeries.add(Duration.ofMillis(System.currentTimeMillis()).minusDays(1).toMillis(), "昨天");
        timeSeries.add(Duration.ofMillis(System.currentTimeMillis()).minusDays(0).toMillis(), "今天");
        timeSeries.add(Duration.ofMillis(System.currentTimeMillis()).plusDays(1).toMillis(), "明天");
        timeSeries.add(Duration.ofMillis(System.currentTimeMillis()).plusDays(2).toMillis(), "后天");
        System.out.println(timeSeries.first(2));
        System.out.println(timeSeries.entryRange(System.currentTimeMillis() - 31231, System.currentTimeMillis() + 2412411));
    }

    @Test
    public void deque() {
        RDeque<Object> deque = redissonClient.getDeque("deque");
        IntStream.range(0, 3).forEach(i -> {
            // 2 1 0 1 1 2 2 3 3
            deque.addFirst(i);
            deque.add(i + 1);
            deque.addLast(i + 1);
        });
        System.out.println(deque);
    }

    @Test
    public void transferQueue() throws InterruptedException {
        // JsonJacksonCodec fails to serialize Throwable on Java17
        // https://github.com/redisson/redisson/issues/5369
        // redisson 3.24.3
        RTransferQueue<Object> tq = redissonClient.getTransferQueue("transfer-queue");
        int total = 10;
        AtomicInteger remaining = new AtomicInteger(total);
        CountDownLatch latch = new CountDownLatch(2);
        pool.execute(() -> {
            IntStream.range(0, total).forEach(i -> {
                try {
                    tq.transfer(i);
                    remaining.decrementAndGet();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            latch.countDown();
        });
        pool.execute(() -> {
            while (remaining.get() > 0 || !tq.isEmpty()) {
                try {
                    tq.take();
                    System.out.println("消费者处理任务中...");
                    ThreadUtil.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            latch.countDown();
        });
        latch.await();
        pool.shutdown();
    }

    @Test
    public void blockingQueueCPTask() {
        blockingQueueCPTask.start();
    }
}
