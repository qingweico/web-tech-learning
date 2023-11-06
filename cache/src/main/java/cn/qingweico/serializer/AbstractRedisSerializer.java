package cn.qingweico.serializer;

import org.springframework.cache.support.NullValue;

import java.util.Objects;

/**
 * @author zqw
 * @date 2023/11/5
 */
public abstract class AbstractRedisSerializer implements RedisSerializer {

    private byte[] nullValueBytes;

    protected static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * 获取空值的序列化值
     *
     * @return byte[]
     */
    public byte[] getNullValueBytes() {
        if (Objects.isNull(nullValueBytes)) {
            synchronized (this) {
                nullValueBytes = serialize(NullValue.INSTANCE);
            }
        }
        return nullValueBytes;
    }
}
