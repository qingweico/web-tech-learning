package cn.qingweico.common.aspect.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouqingwei
 */
@Getter
@AllArgsConstructor
public enum OperationUnit {
    UNKNOWN("unknown"),
    USER("user"),
    Redis("redis");
    private final String value;
}
