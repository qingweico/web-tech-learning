package cn.qingweico.client;

import cn.qingweico.serializer.JacksonRedisSerializer;
import cn.qingweico.serializer.RedisSerializer;
import cn.qingweico.serializer.SerializationException;
import cn.qingweico.serializer.StringRedisSerializer;
import com.alibaba.fastjson.JSON;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zqw
 * @date 2023/11/5
 */
public class SingleRedisClient implements RedisClient {
    Logger logger = LoggerFactory.getLogger(SingleRedisClient.class);
    /**
     * 默认key序列化方式
     */
    private RedisSerializer keySerializer = new StringRedisSerializer();

    /**
     * 默认value序列化方式
     */
    private RedisSerializer valueSerializer = new JacksonRedisSerializer();

    private final StatefulRedisConnection<byte[], byte[]> connection;

    StatefulRedisPubSubConnection<String, String> pubSubConnection;

    public SingleRedisClient(RedisProperties properties) {
        RedisURI redisUri = RedisURI.builder().withHost(properties.getHost())
                .withDatabase(properties.getDatabase())
                .withPort(properties.getPort())
                .build();
        if (StringUtils.isNotBlank(properties.getPassword())) {
            redisUri.setPassword(properties.getPassword().toCharArray());
        }
        logger.info("redis 配置 >>>> {}", JSON.toJSONString(properties));
        io.lettuce.core.RedisClient client = io.lettuce.core.RedisClient.create(redisUri);
        this.connection = client.connect(new ByteArrayCodec());
        this.pubSubConnection = client.connectPubSub();
    }

    @Override
    public <T> T get(String key, Class<T> resultType) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return getValueSerializer().deserialize(sync.get(getKeySerializer().serialize(key)), resultType);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> resultType, RedisSerializer valueRedisSerializer) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return valueRedisSerializer.deserialize(sync.get(getKeySerializer().serialize(key)), resultType);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public String set(String key, Object value) {

        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.set(getKeySerializer().serialize(key), getValueSerializer().serialize(value));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public String set(String key, Object value, long time, TimeUnit unit) {

        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.setex(getKeySerializer().serialize(key), unit.toSeconds(time),
                    getValueSerializer().serialize(value));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public String set(String key, Object value, long time, TimeUnit unit, RedisSerializer valueRedisSerializer) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.setex(getKeySerializer().serialize(key), unit.toSeconds(time),
                    valueRedisSerializer.serialize(value));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public String setNxEx(String key, Object value, long time) {

        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.set(getKeySerializer().serialize(key), getValueSerializer().serialize(value),
                    SetArgs.Builder.nx().ex(time));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long increment(String key) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.incr(getKeySerializer().serialize(key));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long decrement(String key) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.decr(getKeySerializer().serialize(key));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long delete(String... keys) {
        if (Objects.isNull(keys) || keys.length == 0) {
            return 0L;
        }
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();

            final byte[][] byteKeys = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                byteKeys[i] = getKeySerializer().serialize(keys[i]);
            }
            return sync.del(byteKeys);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }


    @Override
    public Long delete(Set<String> keys) {

        return delete(keys.toArray(new String[0]));
    }

    @Override
    public Boolean hasKey(String key) {

        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.exists(getKeySerializer().serialize(key)) > 0;
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.expire(getKeySerializer().serialize(key), timeUnit.toSeconds(timeout));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long getExpire(String key) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.ttl(getKeySerializer().serialize(key));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long lpush(String key, RedisSerializer valueRedisSerializer, String... values) {
        if (Objects.isNull(values) || values.length == 0) {
            return 0L;
        }
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            final byte[][] bv = new byte[values.length][];
            for (int i = 0; i < values.length; i++) {
                bv[i] = valueRedisSerializer.serialize(values[i]);
            }

            return sync.lpush(getKeySerializer().serialize(key), bv);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long rpush(String key, RedisSerializer valueRedisSerializer, String... values) {
        if (Objects.isNull(values) || values.length == 0) {
            return 0L;
        }
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            final byte[][] bv = new byte[values.length][];
            for (int i = 0; i < values.length; i++) {
                bv[i] = valueRedisSerializer.serialize(values[i]);
            }

            return sync.rpush(getKeySerializer().serialize(key), bv);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long llen(String key) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            return sync.llen(getKeySerializer().serialize(key));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end, RedisSerializer valueRedisSerializer) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            List<String> list = new ArrayList<>();
            List<byte[]> values = sync.lrange(getKeySerializer().serialize(key), start, end);
            if (CollectionUtils.isEmpty(values)) {
                return list;
            }
            for (byte[] value : values) {
                list.add(valueRedisSerializer.deserialize(value, String.class));
            }
            return list;
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Set<String> scan(String pattern) {
        Set<String> keys = new HashSet<>();
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            boolean finished;
            ScanCursor cursor = ScanCursor.INITIAL;
            do {
                KeyScanCursor<byte[]> scanCursor = sync.scan(cursor,
                        ScanArgs.Builder.limit(10000).match(pattern));
                scanCursor.getKeys().forEach(key ->
                        keys.add(getKeySerializer().deserialize(key, String.class)));
                finished = scanCursor.isFinished();
                cursor = ScanCursor.of(scanCursor.getCursor());
            } while (!finished);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
        return keys;
    }

    @Override
    public Object eval(String script, List<String> keys, List<Object> args) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            List<byte[]> byteKeys = keys.stream().map(key ->
                    getKeySerializer().serialize(key)).collect(Collectors.toList());
            List<byte[]> byteArgs = args.stream().map(arg ->
                    getValueSerializer().serialize(arg)).collect(Collectors.toList());
            return sync.eval(script, ScriptOutputType.INTEGER, byteKeys.toArray(new byte[0][0]),
                    byteArgs.toArray(new byte[0][0]));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long publish(String channel, String message) {
        try {
            return pubSubConnection.sync().publish(channel, message);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public RedisSerializer getKeySerializer() {
        return keySerializer;
    }

    @Override
    public RedisSerializer getValueSerializer() {
        return valueSerializer;
    }

    @Override
    public void setKeySerializer(RedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    @Override
    public void setValueSerializer(RedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void ltrim(String key, long start, long end) {
        try {
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            sync.ltrim(getKeySerializer().serialize(key), start, end);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public String lpop(String key, RedisSerializer redisSerializer) {
        try {
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            byte[] value = sync.lpop(getKeySerializer().serialize(key));
            if (ArrayUtils.isEmpty(value)) {
                return null;
            }
            return redisSerializer.deserialize(value, String.class);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public <T> List<KeyValue<String, T>> mget(String[] keys, Class<T> resultType) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            byte[][] byteKeys = Arrays.stream(keys).map(k -> getKeySerializer()
                    .serialize(k)).toArray(byte[][]::new);
            return sync.mget(byteKeys).stream().map(v ->
                    KeyValue.just(getKeySerializer().deserialize(v.getKey(), String.class),
                            getValueSerializer().deserialize(v.getValue(), resultType)))
                    .collect(Collectors.toList());
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public void zadd(String key, double score, String member) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            byte[] serializedKey = getKeySerializer().serialize(key);
            byte[] serializedMember = getValueSerializer().serialize(member);
            sync.zadd(serializedKey, score, serializedMember);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long zrem(String key, String... members) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            byte[] serializedKey = getKeySerializer().serialize(key);
            byte[][] serializedMembers = new byte[members.length][];
            for (int i = 0; i < members.length; i++) {
                serializedMembers[i] = getValueSerializer().serialize(members[i]);
            }
            return sync.zrem(serializedKey, serializedMembers);
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> zrangebyscore(String key, long min, long max) {
        try {
            RedisCommands<byte[], byte[]> sync = connection.sync();
            byte[] serializedKey = getKeySerializer().serialize(key);
            Range<Long> scoreRange = Range.create(min, max);
            List<byte[]> rawResult = sync.zrangebyscore(serializedKey, scoreRange);
            return rawResult.stream()
                    .map(e -> getValueSerializer().deserialize(e, String.class))
                    .collect(Collectors.toList());
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }
}
