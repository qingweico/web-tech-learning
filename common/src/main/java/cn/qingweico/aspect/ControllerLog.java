package cn.qingweico.aspect;

import cn.qingweico.constants.Symbol;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zqw
 * @date 2025/12/11
 */
@Aspect
@Slf4j(topic = "controller")
@Component
public class ControllerLog {

    private final ObjectMapper objectMapper;

    public ControllerLog(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(* cn.qingweico..controller..*.*(..))")
    private void pointCut() {
        // pointCut for controller
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        if (ObjectUtils.isEmpty(RequestContextHolder.getRequestAttributes())) {
            return;
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        if (ObjectUtils.isEmpty(sra) || ObjectUtils.isEmpty(sra.getRequest())) {
            return;
        }
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        log.info("Request:[ url: {}, \nmethod: {}, \nuri: {}, \nparams: {}]", url, method, uri, queryString);

        org.aspectj.lang.Signature signature = joinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringTypeName();
        // Controller类名
        String controllerName = declaringTypeName.substring(declaringTypeName.lastIndexOf(Symbol.DOT) + 1);
        // 方法名
        String methodName = signature.getName();
        // 参数名
        Object[] args = joinPoint.getArgs();
        // 记录日志
        try {
            log.info("Method '{}' in class '{}' is called, The parameter list is '{}'"
                    , methodName, controllerName, objectMapper.writeValueAsString(args));
        } catch (JsonProcessingException e) {
            log.info("Method '{}' in class '{}' is called, The parameter list is 'null'"
                    , methodName, controllerName);
        }
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        Signature signature = joinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringTypeName();
        // Controller类名
        String controllerName = declaringTypeName.substring(declaringTypeName.lastIndexOf(Symbol.DOT) + 1);
        // 方法名
        String methodName = signature.getName();
        // 记录日志
        try {
            log.info("{}.{} is called normally", controllerName, methodName);
            log.debug("{}.{} is called normally, and the return value is '{}'"
                    , controllerName, methodName, objectMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            log.info("{}.{} is called normally,  and the return value is 'null'"
                    , controllerName, methodName);
        }
    }

    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        Signature signature = joinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringTypeName();
        // Controller类名
        String controllerName = declaringTypeName.substring(declaringTypeName.lastIndexOf(Symbol.DOT) + 1);
        // 方法名
        String methodName = signature.getName();
        // 记录日志
        log.error("{}.{} throws exception '{}' because of '{}'"
                , controllerName, methodName, e.getClass(), e.getMessage(), e);
    }

}
