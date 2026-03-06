package cn.qingweico.common.aspect.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author zhouqingwei
 */

@Getter
@AllArgsConstructor
public enum OperationType {

    UNKNOWN("unknown"),
    DELETE("delete"),
    SELECT("select"),
    UPDATE("update"),
    INSERT("insert"),
    LOGIN("login");
    private final String value;
}
