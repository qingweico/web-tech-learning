package cn.qingweico.modules.system.aspect;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.OperationLogDetail;
import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.common.util.IPUtils;
import cn.qingweico.common.util.SpringContextUtils;
import cn.qingweico.modules.operationLog.entity.OperationLog;
import cn.qingweico.modules.operationLog.service.IOperationLogService;
import cn.qingweico.modules.system.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouqingwei
 */
@Aspect
@Component
@Slf4j
public class DictAspect {
    @Resource
    private IOperationLogService logService;

    /**
     * 自定义注解
     */
    @Pointcut("@annotation(cn.qingweico.common.aspect.annotation.OperationLogDetail)")
    public void controllerAspect() {

    }

    /**
     * 环绕通知
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("controllerAspect()")
    public Object saveSysLog(ProceedingJoinPoint proceedingJoinPoint) {
        log.info("环绕通知开始>>>>>>>>>>>>>>");
        long startTime;
        long runTime = 0;
        //异常描述
        String exceptionMsg = null;
        //异常代码
        String exceptionCode = null;
        Object result = null;
        try {
            // 获取开始时间
            startTime = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            // 获取方法运行时间
            runTime = System.currentTimeMillis() - startTime;
        } catch (Throwable throwable) {
            exceptionMsg = throwable.getMessage();
            exceptionCode = throwable.getClass().getName();
            throwable.printStackTrace();
        }
        try {
            addPerformLog(proceedingJoinPoint, result, runTime, exceptionMsg, exceptionCode);
        } catch (Throwable throwable) {
            log.info("操作日志新增失败:" + throwable.getMessage());
            throwable.printStackTrace();
        }
        log.info("环绕通知结束>>>>>>>>>>>>>>");
        return result;
    }

    /**
     * 操作日志保存
     *
     * @param joinPoint     切面方法的信息
     * @param ret           返回值
     * @param runTime       方法运行时间
     * @param exceptionMsg  异常描述
     * @param exceptionCode 异常代码
     */
    private void addPerformLog(JoinPoint joinPoint, Object ret, long runTime, String exceptionMsg, String exceptionCode) {
        // 获取当前登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 登录人名称
        String username = null;
        if (null != sysUser) {
            username = sysUser.getUsername();
        } else {
            Result result = (Result) ret;
            if (null != result) {
                Map<String, SysUser> resultMap = (Map<String, SysUser>) result.getResult();
                if (MapUtil.isEmpty(resultMap)) {
                    return;
                }
                SysUser user = resultMap.get("userInfo");
                if (null != user) {
                    username = user.getUsername();
                }
            }
        }

        //过滤后序列化无异常
        Object[] args = joinPoint.getArgs();
        Stream<?> stream = ArrayUtils.isEmpty(args) ? Stream.empty() : Arrays.stream(args);
        List<Object> logArgs = stream
                .filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                .collect(Collectors.toList());
        //返回值
        String params = JSON.toJSONString(logArgs);
        //注解参数值
        Map<String, Object> map = new HashMap<>(16);
        try {
            map = getServiceMethodDescription(joinPoint);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //方法描述
        String describe = (String) map.get("describe");
        //日志等级
        int level = (int) map.get("level");
        //操作类型
        String operationType = (String) map.get("operationType");
        //被操作的对象
        String operationUnit = (String) map.get("operationUnit");
        //方法
        String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        //返回值
        String returnValue = JSON.toJSONString(ret);

        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();

        OperationLog operationLog = new OperationLog();
        operationLog.setLevel(level);
        operationLog.setOperationUnit(operationUnit);
        operationLog.setMethod(method);
        operationLog.setArgs(params);
        operationLog.setUsername(username);
        operationLog.setDescribe(describe);
        operationLog.setOperationType(operationType);
        operationLog.setRunTime((int) runTime);
        operationLog.setReturnValue(returnValue);
        operationLog.setCreated(new Date());
        operationLog.setExceptionMsg(exceptionMsg);
        operationLog.setExceptionCode(exceptionCode);
        operationLog.setIp(IPUtils.getIpAddr(request));

        log.info("记录日志:{}", operationLog);
        logService.save(operationLog);
    }

    /**
     * 获取注解中对方法的描述信息
     *
     * @param joinPoint 切面方法的信息
     * @return
     * @throws ClassNotFoundException
     */
    public static Map<String, Object> getServiceMethodDescription(JoinPoint joinPoint) throws Throwable {
        //注解参数集合
        Map<String, Object> defaultMap = new HashMap<>(16);

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    OperationLogDetail annotation = method.getAnnotation(OperationLogDetail.class);
                    if (null != annotation) {
                        defaultMap.put("level", annotation.level().getValue());
                        defaultMap.put("describe", annotation.detail());
                        defaultMap.put("operationType", annotation.operationType().getValue());
                        defaultMap.put("operationUnit", annotation.operationUnit().getValue());
                        break;
                    }
                }
            }
        }
        return defaultMap;
    }
}
