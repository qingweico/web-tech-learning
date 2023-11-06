package cn.qingweico.core.wrapper;

import cn.qingweico.core.entity.Condition;
import cn.qingweico.core.entity.SelectField;
import cn.qingweico.core.entity.SortCondition;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

/**
 * 条件包装类
 *
 * @author zqw
 * @date 2023/11/04
 */
@Getter
@Setter
public class ConditionWrapper {

    /**
     * 主动查询的集合字段
     */
    private List<SelectField> fields;

    /**
     * 条件构建器参数集合
     */
    private List<Condition> conditions;

    /**
     * 条件构建器排序集合
     */
    private List<SortCondition> sortConditions;

    /**
     * 分页 skip
     */
    private Long skip;

    /**
     * 分页 limit
     */
    private Integer limit;


    public ConditionWrapper(List<SelectField> fields, List<Condition> conditions, List<SortCondition> sortConditions, Long skip, Integer limit) {
        this.fields = fields;
        this.conditions = conditions;
        this.sortConditions = sortConditions;
        this.skip = skip;
        this.limit = limit;
    }
}
