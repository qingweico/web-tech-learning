
package cn.qingweico.common.exception;

import cn.qingweico.common.util.ResultCode;
import org.apache.shiro.authc.AuthenticationException;


/**
 * @author zhouqingwei
 */
public class UserDuplicateLoginException extends AuthenticationException {

    /**
     * Creates a new AuthenticationException.
     */
    public UserDuplicateLoginException() {
        super();
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     */
    public UserDuplicateLoginException(String message) {
        super("{\"code\":\"" + ResultCode.DUPLICATE_LOGIN + "\" ," + "\"message\":\"" + message + "\"}");
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UserDuplicateLoginException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthenticationException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UserDuplicateLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
