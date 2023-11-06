package cn.qingweico.serializer;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.Arrays;

/**
 * >>>>>>>>>>>>>>> Jdk 序列化方式
 *
 * @author zqw
 * @date 2023/11/5
 */
public class JdkRedisSerializer extends AbstractRedisSerializer {
    @Override
    public <T> byte[] serialize(T value) throws SerializationException {
        if (value == null) {
            return EMPTY_ARRAY;
        }

        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires a Serializable payload but received an object of type [" + value.getClass().getName() + "]");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {

            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(String.format("JdkRedisSerializer 序列化异常: %s, %s", e.getMessage(), JSON.toJSONString(value)), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes, Class<T> resultType) throws SerializationException {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }

        if (Arrays.equals(getNullValueBytes(), bytes)) {
            return null;
        }

        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             ObjectInputStream stream = new ObjectInputStream(byteStream)) {
            return (T) stream.readObject();
        } catch (Exception e) {
            throw new SerializationException(String.format("JdkRedisSerializer 反序列化异常: %s, %s", e.getMessage(), JSON.toJSONString(bytes)), e);
        }
    }
}
