package cn.qingweico.serializer;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.util.Arrays;

/**
 * >>>>>>>>>>>>>>> JackJson 序列化方式
 *
 * @author zqw
 * @date 2023/11/5
 */
public class JacksonRedisSerializer extends AbstractRedisSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public <T> byte[] serialize(T value) throws SerializationException {
        if (value == null) {
            return EMPTY_ARRAY;
        }
        try {
            return this.objectMapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new SerializationException(String.format("JacksonRedisSerializer 序列化异常: %s, %s", e.getMessage(), JSON.toJSONString(value)), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> resultType) throws SerializationException {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }

        if (Arrays.equals(getNullValueBytes(), bytes)) {
            return null;
        }

        try {
            return this.objectMapper.readValue(bytes, 0, bytes.length, resultType);
        } catch (Exception e) {
            throw new SerializationException(String.format("JacksonRedisSerializer 反序列化异常: %s, %s", e.getMessage(), JSON.toJSONString(bytes)), e);
        }

    }
}
