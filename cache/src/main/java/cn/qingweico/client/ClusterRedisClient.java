package cn.qingweico.client;

import cn.hutool.core.util.ArrayUtil;
import cn.qingweico.serializer.JdkRedisSerializer;
import cn.qingweico.serializer.RedisSerializer;
import cn.qingweico.serializer.StringRedisSerializer;
import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import io.lettuce.core.*;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zqw
 * @date 2023/11/5
 */
public class ClusterRedisClient implements RedisClient {
    Logger logger = LoggerFactory.getLogger(ClusterRedisClient.class);
    /**
     * 默认key序列化方式
     */
    private RedisSerializer keySerializer = new StringRedisSerializer();

    /**
     * 默认value序列化方式
     */
    private RedisSerializer valueSerializer = new JdkRedisSerializer();

    private final RedisClusterClient cluster;

    private final StatefulRedisClusterConnection<byte[], byte[]> connection;

    private final StatefulRedisPubSubConnection<String, String> pubSubConnection;

    public ClusterRedisClient(RedisProperties properties) {
        logger.info(JSON.toJSONString(properties));

        String cluster = properties.getCluster();
        String[] parts = cluster.split("\\,");
        List<RedisURI> redisUris = new ArrayList<>(parts.length);

        for (String part : parts) {
            HostAndPort hostAndPort = HostAndPort.parse(part);
            RedisURI nodeUri = RedisURI.create(hostAndPort.getHostText(), hostAndPort.hasPort() ? hostAndPort.getPort() : 6379);
            if (StringUtils.isNotBlank(properties.getPassword())) {
                nodeUri.setPassword(properties.getPassword().toCharArray());
            }
            redisUris.add(nodeUri);
        }

        this.cluster = RedisClusterClient.create(redisUris);
        this.connection = this.cluster.connect(new ByteArrayCodec());
        this.pubSubConnection = this.cluster.connectPubSub();
    }

    @Override
    public <T> T get(String key, Class<T> resultType) {
        try {
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            return sync.setex(getKeySerializer().serialize(key), unit.toSeconds(time), getValueSerializer().serialize(value));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public String set(String key, Object value, long time, TimeUnit unit, RedisSerializer valueRedisSerializer) {
        try {
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            return sync.setex(getKeySerializer().serialize(key), unit.toSeconds(time), valueRedisSerializer.serialize(value));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public String setNxEx(String key, Object value, long time) {

        try {
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            return sync.set(getKeySerializer().serialize(key), getValueSerializer().serialize(value), SetArgs.Builder.nx().ex(time));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long increment(String key) {
        try {
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();

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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            return sync.ttl(getKeySerializer().serialize(key));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Set<String> scan(String pattern) {
        Set<String> keys = Collections.synchronizedSet(new HashSet<>());
        try {
            this.cluster.connect(new ByteArrayCodec());
            ScanIterator<byte[]> scan = ScanIterator.scan(connection.sync(), ScanArgs.Builder.limit(10000).match(pattern));
            while (scan.hasNext()) {
                String next = getKeySerializer().deserialize(scan.next(), String.class);
                keys.add(next);
            }
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
        return keys;
    }


    @Override
    public Long lpush(String key, RedisSerializer valueRedisSerializer, String... values) {
        try {
            if (ArrayUtil.isEmpty(values)) {
                return 0L;
            }
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
        try {
            if (Objects.isNull(values) || values.length == 0) {
                return 0L;
            }
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
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
    public Object eval(String script, List<String> keys, List<Object> args) {
        try {
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            List<byte[]> byteKeys = keys.stream().map(key -> getKeySerializer().serialize(key)).collect(Collectors.toList());
            List<byte[]> byteArgs = args.stream().map(arg -> getValueSerializer().serialize(arg)).collect(Collectors.toList());
            return sync.eval(script, ScriptOutputType.INTEGER, byteKeys.toArray(new byte[0][0]), byteArgs.toArray(new byte[0][0]));
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }

    @Override
    public Long publish(String channel, String message) {
        try {
            RedisPubSubCommands<String, String> sync = pubSubConnection.sync();
            return sync.publish(channel, message);
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
            RedisClusterCommands<byte[], byte[]> sync = connection.sync();
            byte[][] byteKeys = Arrays.stream(keys).map(k -> getKeySerializer().serialize(k)).toArray(byte[][]::new);
            return sync.mget(byteKeys).stream().map(v -> KeyValue.just(Arrays.toString(v.getKey()), getValueSerializer().deserialize(v.getValue(), resultType))).collect(Collectors.toList());
        } catch (SerializationException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisClientException(e.getMessage(), e);
        }
    }
}
