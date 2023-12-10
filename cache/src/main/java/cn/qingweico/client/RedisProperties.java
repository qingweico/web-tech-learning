package cn.qingweico.client;

import lombok.Data;

/**
 * @author zqw
 * @date 2023/11/5
 */
@Data
public class RedisProperties {
    String host;
    String password;
    Integer database;
    Integer port;
    /**
     * 序列化方式
     */
    String serializer;
}
