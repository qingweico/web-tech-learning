package cn.qingweico.client;

import cn.qingweico.serializer.RedisSerializer;
import io.lettuce.core.KeyValue;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zqw
 * @date 2023/11/5
 */
public interface RedisClient {
    /**
     * 获取RedisClient实例
     *
     * @param redisProperties redis配置
     * @return RedisClient
     */
    static RedisClient getInstance(RedisProperties redisProperties) {
        return new SingleRedisClient(redisProperties);
    }

    /**
     * 通过key获取储存在redis中的value,自动转对象
     *
     * @param key        key
     * @param resultType 返回值类型对应的Class对象
     * @param <T>        返回值类型
     * @return 成功返回value 失败返回null
     */
    <T> T get(String key, Class<T> resultType);

    /**
     * 通过key获取储存在redis中的value,自动转对象
     *
     * @param key                  key
     * @param resultType           返回值类型对应的Class对象
     * @param valueRedisSerializer 指定序列化器
     * @param <T>                  返回值类型
     * @return 成功返回value 失败返回null
     */
    <T> T get(String key, Class<T> resultType, RedisSerializer valueRedisSerializer);

    /**
     * <p>
     * 向redis存入key和value,并释放连接资源
     * </p>
     * <p>
     * 如果key已经存在 则覆盖
     * </p>
     *
     * @param key   key
     * @param value value
     * @return 成功 返回OK 失败返回 0
     */
    String set(String key, Object value);

    /**
     * <p>
     * 向redis存入key和value,并释放连接资源
     * </p>
     * <p>
     * 如果key已经存在 则覆盖
     * </p>
     *
     * @param key   key
     * @param value value
     * @param time  时间
     * @param unit  时间单位
     * @return 成功 返回OK 失败返回 0
     */
    String set(String key, Object value, long time, TimeUnit unit);

    /**
     * <p>
     * 向redis存入key和value,并释放连接资源
     * </p>
     * <p>
     * 如果key已经存在 则覆盖
     * </p>
     *
     * @param key                  key
     * @param value                value
     * @param time                 时间
     * @param unit                 时间单位
     * @param valueRedisSerializer 指定序列化器
     * @return 成功 返回OK 失败返回 0
     */
    String set(String key, Object value, long time, TimeUnit unit, RedisSerializer valueRedisSerializer);

    /**
     * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
     * GB).
     * -------------------------- 互斥 --------------------------
     * nx : key 不存在 可以设置, 存在 不会覆盖  返回 null
     * xx : key 存在不存在 都可以设置(覆盖)
     * -------------------------- 互斥 --------------------------
     * ex : key 的超时秒数
     * px : key 的超时毫秒数
     *
     * @param key   key
     * @param value value
     * @param time  expire time in the units of <code>ex px</code>
     * @return Status code reply
     */
    String setNxEx(final String key, final Object value, final long time);


    /**
     * <p>
     * Increment the integer value of a key by one.
     * 获取指定key的值进行加1
     * 如果 key 不存在, 那么 key 的值会先被初始化为 0 , 然后再执行 INCR 操作
     * </p>
     *
     * @param key key
     * @return Long integer-reply the value of {@code key} after the increment
     */
    Long increment(String key);

    /**
     * <p>
     * Decrement the integer value of a key by one
     * 获取指定key的值进行减1
     * 如果 key 不存在, 那么 key 的值会先被初始化为 0 , 然后再执行 DECR 操作
     * </p>
     *
     * @param key key
     * @return Long integer-reply the value of {@code key} after the decrement
     */
    Long decrement(String key);

    /**
     * <p>
     * 删除指定的key,也可以传入一个包含key的数组
     * </p>
     *
     * @param keys 一个key 也可以使 string 数组
     * @return 返回删除成功的个数
     */
    Long delete(String... keys);

    /**
     * <p>
     * 删除一批key
     * </p>
     *
     * @param keys key的Set集合
     * @return 返回删除成功的个数
     */
    Long delete(Set<String> keys);


    /**
     * <p>
     * 判断key是否存在
     * </p>
     *
     * @param key key
     * @return true OR false
     */
    Boolean hasKey(String key);

    /**
     * <p>
     * 为给定 key (前提是该key已存在)设置生存时间(ttl), 当 key 过期时(生存时间为 0 ), 它会被自动删除.
     * </p>
     *
     * @param key      key
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return true OR false
     */
    Boolean expire(String key, long timeout, TimeUnit timeUnit);


    /**
     * <p>
     * 以秒为单位, 返回给定 key 的剩余生存时间
     * </p>
     *
     * @param key key
     * @return 当 key 不存在时或没有设置剩余生存时间时, 返回 -1 . 否则, 以秒为单位, 返回 key 的剩余生存时间.
     */
    Long getExpire(String key);

    /**
     * <p>
     * 查询符合条件的key
     * </p>
     *
     * @param pattern 表达式
     * @return 返回符合条件的key
     */
    Set<String> scan(String pattern);

    /**
     * <p>
     * 通过key向list首部添加字符串
     * </p>
     *
     * @param key                  key
     * @param valueRedisSerializer 指定序列化器
     * @param values               variable parameters
     * @return 返回list的value个数
     */
    Long lpush(String key, RedisSerializer valueRedisSerializer, String... values);

    /**
     * <p>
     * 通过key向list尾部添加字符串
     * </p>
     *
     * @param key                  key
     * @param valueRedisSerializer 指定序列化器
     * @param values               variable parameters
     * @return 返回list的value个数
     */
    Long rpush(String key, RedisSerializer valueRedisSerializer, String... values);

    /**
     * <p>
     * 通过key返回list的长度
     * </p>
     *
     * @param key key
     * @return long
     */
    Long llen(String key);

    /**
     * <p>
     * 通过key获取list指定下标位置的value
     * </p>
     * <p>
     * 如果start 为 0 end 为 -1 则返回全部的list中的value
     * </p>
     *
     * @param key                  key
     * @param start                起始位置
     * @param end                  结束位置(包含)
     * @param valueRedisSerializer 指定序列化器
     * @return List
     */
    List<String> lrange(String key, long start, long end, RedisSerializer valueRedisSerializer);

    /**
     * 执行Lua脚本
     *
     * @param script Lua 脚本
     * @param keys   参数
     * @param args   参数值
     * @return 返回结果
     */
    Object eval(String script, List<String> keys, List<Object> args);

    /**
     * 发送消息
     *
     * @param channel 发送消息的频道
     * @param message 消息内容
     * @return 接收到信息的订阅者数量
     */
    Long publish(String channel, String message);

    /**
     * key序列化方式
     *
     * @return the key {@link RedisSerializer}.
     */
    RedisSerializer getKeySerializer();

    /**
     * value序列化方式
     *
     * @return the value {@link RedisSerializer}.
     */
    RedisSerializer getValueSerializer();

    /**
     * 设置key的序列化方式
     *
     * @param keySerializer {@link RedisSerializer}
     */
    void setKeySerializer(RedisSerializer keySerializer);

    /**
     * 设置value的序列化方式
     *
     * @param valueSerializer {@link RedisSerializer}
     */
    void setValueSerializer(RedisSerializer valueSerializer);

    /**
     * <p>
     * 通过key保留list指定下标位置的value
     * </p>
     * <p>
     * 如果start 为 0 end 为 -1 则保留list中所有数据
     * </p>
     *
     * @param key   key
     * @param start 起始位置
     * @param end   结束位置
     */
    void ltrim(String key, long start, long end);

    /**
     * 移除并返回列表首部的第一个元素
     *
     * @param key             key
     * @param redisSerializer RedisSerializer
     * @return String
     */
    String lpop(String key, RedisSerializer redisSerializer);

    /**
     * 通过keys获取储存在redis中的value,自动转对象
     *
     * @param keys       keys
     * @param resultType 返回值类型对应的Class对象
     * @param <T>        返回值类型
     * @return 成功返回value 失败返回null
     */
    <T> List<KeyValue<String, T>> mget(String[] keys, Class<T> resultType);
}
