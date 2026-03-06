package cn.qingweico.common.aspect.annotation;


import lombok.Getter;

/**
 * 操作日志级别枚举
 *
 * @author zhouqingwei
 */

@Getter
public enum LogLevelType {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);
    private final int value;

    LogLevelType(int s) {
        this.value = s;
    }
}
