package cn.qingweico.common.aspect.annotation;


import java.lang.annotation.*;


/**
 * @author zhouqingwei
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogDetail {

    /**
     * 方法描述:可使用占位符获取参数:{{tel}}
     */
    String detail() default "";

    /**
     * 日志等级 {@link LogLevelType}
     */
    LogLevelType level() default LogLevelType.ONE;

    /**
     * 操作类型(enum) {@link OperationType}
     */
    OperationType operationType() default OperationType.UNKNOWN;

    /**
     * 被操作的对象 {@link OperationUnit}
     */
    OperationUnit operationUnit() default OperationUnit.UNKNOWN;
}
