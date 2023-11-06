package cn.qingweico.core.entity;


import cn.qingweico.core.constant.ConditionType;
import cn.qingweico.core.constant.OperatorType;
import cn.qingweico.core.wrapper.ConditionWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 条件
 *
 * @author zqw
 * @date 2023/11/04
 */
@Getter
@Setter
public class Condition {

    public Condition() {
    }

    public Condition(OperatorType type, String col, List<Object> args) {
        this.type = type;
        this.col = col;
        this.args = args;
    }

    /**
     * 嵌套条件比较类型
     */
    private ConditionType conditionType = ConditionType.AND;

    /**
     * 比较类型
     */
    private OperatorType type;

    /**
     * 集合对应的列
     */
    private String col;

    /**
     * 比较值
     */
    private List<Object> args;

    /**
     * 条件包装类
     */
    private ConditionWrapper conditionWrapper;
}
