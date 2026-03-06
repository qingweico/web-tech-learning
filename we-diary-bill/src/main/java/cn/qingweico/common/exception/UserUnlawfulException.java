
package cn.qingweico.common.exception;

import cn.qingweico.common.util.ResultCode;
import org.apache.shiro.authc.AuthenticationException;


/**
 * @author zhouqingwei
 */
public class UserUnlawfulException extends AuthenticationException {

    /**
     * Creates a new AuthenticationException.
     */
    public UserUnlawfulException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     */
    public UserUnlawfulException(String message) {
        super("{\"code\":\"" + ResultCode.USER_UNLAWFUL + "\" ," + "\"message\":\"" + message + "\"}");
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UserUnlawfulException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UserUnlawfulException(String message, Throwable cause) {
        super(message, cause);
    }
}
