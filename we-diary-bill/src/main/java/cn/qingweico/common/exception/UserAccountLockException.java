
package cn.qingweico.common.exception;

import cn.qingweico.common.util.ResultCode;
import org.apache.shiro.authc.AuthenticationException;


/**
 * @author zhouqingwei
 */
public class UserAccountLockException extends AuthenticationException {

    /**
     * Creates a new AuthenticationException.
     */
    public UserAccountLockException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     */
    public UserAccountLockException(String message) {
        super("{\"code\":" + ResultCode.USER_ACCOUNT_LOCK + " ," + "\"message\":\"" + message + "\"}");
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UserAccountLockException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UserAccountLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
