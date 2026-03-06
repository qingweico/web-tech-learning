
package cn.qingweico.common.exception;

import cn.qingweico.common.util.ResultCode;
import org.apache.shiro.authc.AuthenticationException;


/**
 * @author zhouqingwei
 */
public class TokenNullException extends AuthenticationException {

    /**
     * Creates a new AuthenticationException.
     */
    public TokenNullException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     */
    public TokenNullException(String message) {
        super("{\"code\":\"" + ResultCode.TOKEN_NULL + "\" ," + "\"message\":\"" + message + "\"}");
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public TokenNullException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public TokenNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
