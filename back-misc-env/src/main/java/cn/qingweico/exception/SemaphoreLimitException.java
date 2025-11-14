package cn.qingweico.exception;

/**
 * @author zqw
 * @date 2025/11/12
 */
public class SemaphoreLimitException extends RuntimeException {

    public SemaphoreLimitException(String message) {
        super(message);
    }

    public SemaphoreLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
