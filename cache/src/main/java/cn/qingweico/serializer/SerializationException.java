package cn.qingweico.serializer;

import org.springframework.core.NestedRuntimeException;

/**
 * SerializationException
 *
 * @author zqw
 * @date 2023/11/5
 */
public class SerializationException extends NestedRuntimeException {
    /**
     * Constructs a new <code>SerializationException</code> instance.
     *
     * @param msg   msg
     * @param cause Throwable
     */
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
