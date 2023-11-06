package cn.qingweico.core.wrapper;

import cn.qingweico.core.constant.ConditionType;
import cn.qingweico.core.constant.OperatorType;
import cn.qingweico.core.constant.SortType;
import cn.qingweico.core.entity.Condition;
import cn.qingweico.core.entity.SelectField;
import cn.qingweico.core.entity.SortCondition;
import cn.qingweico.core.sdk.base.Compare;
import cn.qingweico.core.sdk.base.Func;
import cn.qingweico.core.sdk.base.Nested;
import cn.qingweico.core.sdk.base.InterFunction;
import cn.qingweico.utils.ConvertUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 条件构建器
 *
 * @author zqw
 * @date 2023/11/04
 */
public class LambdaQueryWrapper<T>
        implements
        Compare<LambdaQueryWrapper<T>, InterFunction<T, ?>>,
        Func<T, LambdaQueryWrapper<T>, InterFunction<T, ?>>,
        Nested<LambdaQueryWrapper<T>, LambdaQueryWrapper<T>> {

    private final List<SelectField> fields = new ArrayList<>(5);
    private final List<Condition> conditions = new ArrayList<>(5);
    private final List<SortCondition> sortConditions = new ArrayList<>(5);
    private Long skip;
    private Integer limit;

    public ConditionWrapper getCondition() {
        return new ConditionWrapper(fields, conditions, sortConditions, skip, limit);
    }

    private String getFieldMeta(InterFunction<T, ?> column) {
        return ConvertUtil.convertToFieldName(column);
    }

    @Override
    public LambdaQueryWrapper<T> eq(boolean condition, InterFunction<T, ?> column, Object val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.EQ, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> ne(boolean condition, InterFunction<T, ?> column, Object val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.NE, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> le(boolean condition, InterFunction<T, ?> column, Object val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.LE, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> lt(boolean condition, InterFunction<T, ?> column, Object val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.LT, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> ge(boolean condition, InterFunction<T, ?> column, Object val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.GE, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> gt(boolean condition, InterFunction<T, ?> column, Object val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.GT, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> between(boolean condition, InterFunction<T, ?> column, Object leftV, Object rightV) {
        if (condition) {
            conditions.add(new Condition(OperatorType.BW, getFieldMeta(column), Arrays.asList(leftV, rightV)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> in(boolean condition, InterFunction<T, ?> column, Collection<Object> val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.IN, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> notIn(boolean condition, InterFunction<T, ?> column, Collection<Object> val) {
        if (condition) {
            conditions.add(new Condition(OperatorType.NIN, getFieldMeta(column), Collections.singletonList(val)));
        }
        return this;
    }


    @Override
    public LambdaQueryWrapper<T> and(boolean condition, Function<LambdaQueryWrapper<T>, LambdaQueryWrapper<T>> func) {

        if (condition) {
            LambdaQueryWrapper<T> apply = func.apply(new LambdaQueryWrapper<>());
            ConditionWrapper conditionWrapper = apply.getCondition();
            Condition c = new Condition();
            c.setConditionWrapper(conditionWrapper);
            this.conditions.add(c);
        }
        return this;

    }

    @Override
    public LambdaQueryWrapper<T> or() {

        if (conditions.size() == 0) {
            throw new RuntimeException("not first use or");
        }
        Condition lastCondition = conditions.get(conditions.size() - 1);
        ConditionWrapper conditionWrapper = lastCondition.getConditionWrapper();
        if (Objects.isNull(conditionWrapper)) {
            conditionWrapper = new ConditionWrapper(fields, conditions, sortConditions, skip, limit);
        }
        List<Condition> sub = conditionWrapper.getConditions();
        Condition condition = sub.get(sub.size() - 1);
        condition.setConditionType(ConditionType.OR);
        return this;

    }

    @Override
    public final LambdaQueryWrapper<T> orderByAsc(boolean condition, InterFunction<T, ?> column) {

        if (condition) {
            appendSortField(column, SortType.ASC);
        }
        return this;

    }

    @Override
    public final LambdaQueryWrapper<T> orderByDesc(boolean condition, InterFunction<T, ?> column) {

        if (condition) {
            appendSortField(column, SortType.DESC);
        }
        return this;

    }

    @Override
    public LambdaQueryWrapper<T> skip(Long skip) {

        this.skip = skip;
        return this;

    }

    @Override
    public LambdaQueryWrapper<T> limit(Integer limit) {

        this.limit = limit;
        return this;

    }

    @SafeVarargs
    @Override
    public final LambdaQueryWrapper<T> select(InterFunction<T, ?>... columns) {

        if (Objects.nonNull(columns) && columns.length > 0) {
            List<SelectField> fields = Arrays.stream(columns).map(column -> new SelectField(getFieldMeta(column))).collect(Collectors.toList());
            this.fields.addAll(fields);
        }
        return this;

    }

    private void appendSortField(InterFunction<T, ?> column, SortType sortType) {

        sortConditions.add(new SortCondition(sortType, getFieldMeta(column)));

    }

}
