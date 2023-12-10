package cn.qingweico;

import cn.hutool.core.date.DateUtil;
import cn.qingweico.client.RedisClient;
import cn.qingweico.delayTask.DelayQueueManager;
import cn.qingweico.delayTask.DelayTask;
import cn.qingweico.delayTask.RedisBroadcastDelayTask;
import cn.qingweico.serializer.JacksonRedisSerializer;
import cn.qingweico.serializer.JdkRedisSerializer;
import cn.qinwweico.entity.User;
import cn.qinwweico.util.RandomDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zqw
 * @date 2023/11/6
 */
@SpringBootTest
@Slf4j
public class RedisApplicationTests {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private ExecutorService pool;

    @Autowired
    private DelayQueueManager delayQueueManager;

    @Test
    void strType() {
        for (int i = 0; i < 10; i++) {
            redisClient.set("key" + i, "value" + i);
        }
    }

    @Test
    void objType() {
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .id(String.valueOf(i))
                    .username(RandomDataUtil.name())
                    .mobile(RandomDataUtil.phone())
                    .birthday(RandomDataUtil.birthday())
                    .address(RandomDataUtil.address())
                    .build();
            System.out.println(redisClient.set("user[" + i + "]", user));
        }
    }

    @Test
    void getObj() {
        System.out.println(redisClient.get("user[0]", User.class));
    }

    @Test
    void getObjAssignedSerializer() {
        User user = User.builder()
                .id(String.valueOf(10))
                .username(RandomDataUtil.name())
                .mobile(RandomDataUtil.phone())
                .birthday(RandomDataUtil.birthday())
                .address(RandomDataUtil.address())
                .build();
        System.out.println(redisClient.set("user[" + 10 + "]", user, 10, TimeUnit.SECONDS, new JdkRedisSerializer()));
        System.out.println(redisClient.get("user[10]", User.class, new JdkRedisSerializer()));
    }

    @Test
    void setValueExpired() {
        System.out.println(redisClient.set("SmsCode", "351093", 10, TimeUnit.SECONDS));
    }

    @Test
    void setNxEx() {
        System.out.println(redisClient.setNxEx("lock-prefix", UUID.randomUUID().toString(), 10));
        String OK = "OK";
        String result;
        System.out.println(DateUtil.now());
        do {
            result = redisClient.setNxEx("lock-prefix", UUID.randomUUID().toString(), 10);
        } while (!OK.equals(result));
        System.out.println(DateUtil.now());
    }

    @Test
    void increment() {
        System.out.println(redisClient.increment("PV"));
    }

    @Test
    void decrement() {
        System.out.println(redisClient.decrement("UV"));
    }

    @Test
    void delete() {
        HashSet<String> set = new HashSet<>();
        set.add("PV");
        set.add("UV");
        System.out.println(redisClient.delete(set));
    }

    @Test
    void hasKey() {
        System.out.println(redisClient.hasKey("SmsCode"));
    }

    @Test
    void expire() throws InterruptedException {
        System.out.println(redisClient.expire("key1", 10, TimeUnit.SECONDS));
        TimeUnit.SECONDS.sleep(3);
        System.out.println(redisClient.getExpire("key1"));
    }

    @Test
    void scan() {
        System.out.println(redisClient.scan("key*"));
    }

    @Test
    void lpush() {
        System.out.println(redisClient.lpush("NumberArray", new JacksonRedisSerializer(), "1", "2", "3", "4", "5"));
    }

    @Test
    void rpush() {
        System.out.println(redisClient.rpush("CharArray", new JacksonRedisSerializer(), "a", "b", "c", "d", "e"));
    }

    @Test
    void llen() {
        System.out.println(redisClient.llen("CharArray"));
    }

    @Test
    void lrange() {
        System.out.println(redisClient.lrange("CharArray", 1, 3, new JacksonRedisSerializer()));
    }

    @Test
    void lua() throws IOException {
        DefaultRedisScript<Boolean> casScript = new DefaultRedisScript<>();
        casScript.setResultType(Boolean.class);
        casScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("cas.lua")));
        String script = casScript.getScriptAsString();
        List<String> keys = new ArrayList<>();
        keys.add("AppliedNumber");
        List<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            pool.execute(() -> log.info("第 {} 次, {}", finalI, redisClient.eval(script, keys, args)));
        }
        pool.shutdown();
        // Simple block
        System.out.println(System.in.read());
    }

    @Test
    void publish() {
        System.out.println(redisClient.publish("EmailChannel", "Hello Email"));
    }

    @Test
    void ltrim() {
        System.out.println(redisClient.lpush("NumberArray", new JacksonRedisSerializer(), "1", "2", "3", "4", "3", "2", "1"));
        redisClient.ltrim("NumberArray", 2L, 4L);
        System.out.println(redisClient.lrange("NumberArray", 0, -1, new JacksonRedisSerializer()));
    }

    @Test
    void lpop() {
        System.out.println(redisClient.lpop("NumberArray", new JacksonRedisSerializer()));
    }

    @Test
    void mget() {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            keys.add("user[" + i + "]");
        }
        System.out.println(redisClient.mget(keys.toArray(new String[]{}), User.class));
    }

    @Test
    void delayMessage() throws IOException {
        final long DELAY_MILLIS = 5 * 1000;
        long now = System.currentTimeMillis();
        long delay = now + DELAY_MILLIS;
        delayQueueManager.put
                (new DelayTask
                        (new RedisBroadcastDelayTask("EmailChannel", String.valueOf(delay)),
                                delay));


        System.out.println(System.in.read());
    }
}
