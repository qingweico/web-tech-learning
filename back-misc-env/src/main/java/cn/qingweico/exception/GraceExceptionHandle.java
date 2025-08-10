package cn.qingweico.exception;

import cn.qingweico.model.ApiResponse;
import cn.qingweico.model.ParamValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Slf4j
@ControllerAdvice
public class GraceExceptionHandle {
    @ExceptionHandler({ParamValidateException.class})
    @ResponseBody
    public ApiResponse<?> paramValidateExceptionHandler(ParamValidateException ex) {
        return ApiResponse.error(ex.getMessage());
    }
}
