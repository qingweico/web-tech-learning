package cn.qingweico.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.qingweico.model.ParamValidateException;
import cn.qingweico.validate.ValidatorHelper;
import cn.qingweico.validate.anno.ParamValid;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Aspect
public class GlobalValidateAspect implements Ordered {

    private final ValidatorHelper validatorHelper;

    public GlobalValidateAspect(ValidatorHelper validatorHelper) {
        this.validatorHelper = validatorHelper;
    }

    //切入所有控制器方法
    @Pointcut("(@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController) )" +
            "&& ( @annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping) || @annotation(org.springframework.web.bind.annotation.PatchMapping) )")
    public void exec() {
    }

    @Before("exec()")
    public void execute(JoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        if (annotations.length > 0) {
            Object[] args = pjp.getArgs();
            for (int i = 0; i < args.length; i++) {
                Annotation[] ans = annotations[i];
                for (Annotation a : ans) {
                    if (a instanceof ParamValid) {
                        if (ObjectUtil.isEmpty(args[i])) {
                            Parameter p = method.getParameters()[i];
                            Map<String, String> param = new HashMap<>();
                            param.put(p.getName(), "请求参数[" + p.getName() + "]不可为空");
                            throw new ParamValidateException(param);
                        }
                        validatorHelper.validateForException(args[i]);
                    } else if (a instanceof RequestParam && ((RequestParam) a).required() && ObjectUtil.isEmpty(args[i])) {
                        Parameter p = method.getParameters()[i];
                        Map<String, String> param = new HashMap<>();
                        param.put(p.getName(), "请求参数[" + p.getName() + "]不可为空");
                        throw new ParamValidateException(param);
                    } else if (a instanceof RequestHeader && ((RequestHeader) a).required() && ObjectUtil.isEmpty(args[i])) {
                        Parameter p = method.getParameters()[i];
                        Map<String, String> param = new HashMap<>();
                        param.put(p.getName(), "请求参数[" + p.getName() + "]不可为空");
                        throw new ParamValidateException(param);
                    } else if (a instanceof PathVariable && ((PathVariable) a).required() && ObjectUtil.isEmpty(args[i])) {
                        Parameter p = method.getParameters()[i];
                        Map<String, String> param = new HashMap<>();
                        param.put(p.getName(), "请求参数[" + p.getName() + "]不可为空");
                        throw new ParamValidateException(param);
                    } else if (a instanceof CookieValue && ((CookieValue) a).required() && ObjectUtil.isEmpty(args[i])) {
                        Parameter p = method.getParameters()[i];
                        Map<String, String> param = new HashMap<>();
                        param.put(p.getName(), "请求参数[" + p.getName() + "]不可为空");
                        throw new ParamValidateException(param);
                    } else if (a instanceof SessionAttribute && ((SessionAttribute) a).required() && ObjectUtil.isEmpty(args[i])) {
                        Parameter p = method.getParameters()[i];
                        Map<String, String> param = new HashMap<>();
                        param.put(p.getName(), "请求参数[" + p.getName() + "]不可为空");
                        throw new ParamValidateException(param);
                    }
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return 50;
    }
}
