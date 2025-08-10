package cn.qingweico.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/1
 */
@Setter
@Getter
public class ParamValidateException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -9197413010627370100L;

    private Map<String, String> error;


    public ParamValidateException() {
        super(BaseErrorCode.VALIDATE_FAILED);
    }

    public ParamValidateException(Map<String, String> error) {
        this();
        this.error = error;
    }

    public ParamValidateException(String msg) {
        super(BaseErrorCode.VALIDATE_FAILED, msg);
    }

    public ParamValidateException(String msg, Map<String, String> error) {
        super(BaseErrorCode.VALIDATE_FAILED, msg);
        this.error = error;
    }

    public ParamValidateException(Throwable e) {
        super(BaseErrorCode.VALIDATE_FAILED, e);
    }

    public ParamValidateException(String msg, Throwable e) {
        super(BaseErrorCode.VALIDATE_FAILED, msg, e);
    }

    @Override
    public String getMessage() {
        if (error != null) {
            return StringUtils.join(error.values(), ";");
        } else if (super.getMessage() != null) {
            return super.getMessage();
        } else {
            return super.getMessage();
        }
    }
}
