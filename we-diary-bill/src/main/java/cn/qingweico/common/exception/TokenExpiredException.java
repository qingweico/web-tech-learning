
package cn.qingweico.common.exception;

import cn.qingweico.common.util.ResultCode;
import org.apache.shiro.authc.AuthenticationException;


/**
 * @author zhouqingwei
 */
public class TokenExpiredException extends AuthenticationException
{

    /**
     * Creates a new AuthenticationException.
     */
    public TokenExpiredException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     */
    public TokenExpiredException(String message) {
        super("{\"code\":\"" + ResultCode.EXPIRED_TOKEN + "\" ," + "\"message\":\"" + message + "\"}");
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public TokenExpiredException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
