
package cn.qingweico.common.exception;

import cn.qingweico.common.util.ResultCode;
import org.apache.shiro.authc.AuthenticationException;


/**
 * @author zhouqingwei
 */
public class UserNotExistException extends AuthenticationException {

    /**
     * Creates a new AuthenticationException.
     */
    public UserNotExistException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     */
    public UserNotExistException(String message) {
        super("{\"code\":\"" + ResultCode.USER_NOT_EXIST + "\" ," + "\"message\":\"" + message + "\"}");
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UserNotExistException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
