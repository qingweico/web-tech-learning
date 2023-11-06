package cn.qingweico.client;

/**
 * @author zqw
 * @date 2023/11/5
 */
public class RedisClientException extends RuntimeException {
    public RedisClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
