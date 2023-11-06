package cn.qingweico.utils;

import cn.qingweico.core.constant.ConditionType;
import cn.qingweico.core.constant.OperatorType;
import cn.qingweico.core.constant.SortType;
import cn.qingweico.core.entity.Condition;
import cn.qingweico.core.entity.SortCondition;
import cn.qingweico.core.wrapper.ConditionWrapper;
import cn.qingweico.core.wrapper.LambdaQueryWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 查询工具构建工具
 *
 * @author zqw
 * @date 2023/11/04
 */
public class QueryBuildUtils {

    private static final Map<OperatorType, Function<Condition, Criteria>> HANDLERS = new ConcurrentHashMap<>();

    static {
        HANDLERS.put(OperatorType.EQ, QueryBuildUtils::eqHandle);
        HANDLERS.put(OperatorType.NE, QueryBuildUtils::neHandle);
        HANDLERS.put(OperatorType.LE, QueryBuildUtils::leHandle);
        HANDLERS.put(OperatorType.LT, QueryBuildUtils::ltHandle);
        HANDLERS.put(OperatorType.GE, QueryBuildUtils::geHandle);
        HANDLERS.put(OperatorType.GT, QueryBuildUtils::gtHandle);
        HANDLERS.put(OperatorType.BW, QueryBuildUtils::bwHandle);
        HANDLERS.put(OperatorType.IN, QueryBuildUtils::inHandle);
        HANDLERS.put(OperatorType.NIN, QueryBuildUtils::ninHandle);
    }

    /**
     * 使用条件构建器构建查询条件
     *
     * @param queryWrapper 条件构建器
     * @return 查询条件
     */
    public static Query buildQuery(LambdaQueryWrapper<?> queryWrapper) {
        return buildQuery(queryWrapper.getCondition());
    }

    /**
     * 使用条件构建器构建查询条件
     *
     * @param arg 条件构建器封装的真实条件参数
     * @return 查询条件
     */
    private static Query buildQuery(ConditionWrapper arg) {

        Criteria criteria = new Criteria();
        // 01 构建查询参数
        criteria.andOperator(buildCondition(arg));
        Query query = new Query(criteria);
        List<SortCondition> sortConditions = arg.getSortConditions();

        // 02 构建排序参数
        if (Objects.nonNull(sortConditions)) {
            query.with(Sort.by(buildSort(sortConditions)));
        }

        // 03 构建分页参数
        if (Objects.nonNull(arg.getSkip())) {
            query.skip(arg.getSkip());
        }
        if (Objects.nonNull(arg.getLimit())) {
            query.limit(arg.getLimit());
        }

        // 04 构建查询列
        if (Objects.nonNull(arg.getFields()) && arg.getFields().size() > 0) {
            Field fields = query.fields();
            arg.getFields().forEach(item -> fields.include(item.getCol()));
        }

        return query;

    }

    /**
     * 构建排序信息
     *
     * @param sortConditions 排序条件
     * @return 排序信息
     */
    private static List<Sort.Order> buildSort(List<SortCondition> sortConditions) {

        return sortConditions.stream().map(item -> {
            if (item.getSortType() == SortType.ASC) {
                return Sort.Order.asc(item.getCol());
            } else {
                return Sort.Order.desc(item.getCol());
            }
        }).collect(Collectors.toList());

    }

    /**
     * 构建查询条件
     *
     * @param arg 条件构造器中的条件参数
     * @return 查询条件
     */
    private static Criteria[] buildCondition(ConditionWrapper arg) {

        Criteria criteria = new Criteria();
        if (Objects.isNull(arg) || Objects.isNull(arg.getConditions()) || arg.getConditions().size() == 0) {
            return new Criteria[]{criteria};
        }
        List<Condition> conditions = arg.getConditions();

        boolean isOr = false;
        Criteria[] critters = new Criteria[conditions.size()];
        for (int index = 0; index < conditions.size(); index++) {
            Condition condition = conditions.get(index);
            if (Objects.nonNull(condition.getConditionWrapper()) && Objects.isNull(condition.getCol()) && condition.getConditionWrapper().getConditions().size() > 0) {
                Criteria curCriteria = new Criteria();
                Condition first = condition.getConditionWrapper().getConditions().get(0);
                if (first.getConditionType() == ConditionType.OR) {
                    curCriteria.orOperator(buildCondition(condition.getConditionWrapper()));
                } else {
                    curCriteria.andOperator(buildCondition(condition.getConditionWrapper()));
                }
                critters[index] = curCriteria;
                continue;
            }
            OperatorType type = condition.getType();
            if (condition.getConditionType() == ConditionType.OR) {
                isOr = true;
            }
            Function<Condition, Criteria> handler = HANDLERS.get(type);
            if (Objects.isNull(handler)) {
                throw new RuntimeException(String.format("buildQuery error not have queryType %s", type));
            }
            Criteria curCriteria = handler.apply(condition);
            critters[index] = curCriteria;
        }
        if (isOr) {
            criteria.orOperator(critters);
        } else {
            criteria.andOperator(critters);
        }
        return critters;

    }

    /**
     * eq 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria eqHandle(Condition condition) {
        return Criteria.where(condition.getCol()).is(condition.getArgs().get(0));
    }

    /**
     * ne 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria neHandle(Condition condition) {
        return Criteria.where(condition.getCol()).ne(condition.getArgs().get(0));
    }

    /**
     * le 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria leHandle(Condition condition) {
        return Criteria.where(condition.getCol()).lte(condition.getArgs().get(0));
    }

    /**
     * lt 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria ltHandle(Condition condition) {
        return Criteria.where(condition.getCol()).lt(condition.getArgs().get(0));
    }

    /**
     * ge 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria geHandle(Condition condition) {
        return Criteria.where(condition.getCol()).gte(condition.getArgs().get(0));
    }

    /**
     * gt 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria gtHandle(Condition condition) {
        return Criteria.where(condition.getCol()).gt(condition.getArgs().get(0));
    }

    /**
     * between 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria bwHandle(Condition condition) {
        return Criteria.where(condition.getCol()).lte(condition.getArgs().get(0)).gt(condition.getArgs().get(1));
    }

    /**
     * in 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    @SuppressWarnings("unchecked")
    private static Criteria inHandle(Condition condition) {
        List<Object> args = (List<Object>) condition.getArgs().get(0);
        return Criteria.where(condition.getCol()).in(args.toArray());
    }

    /**
     * notIn 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    @SuppressWarnings("unchecked")
    private static Criteria ninHandle(Condition condition) {
        List<Object> args = (List<Object>) condition.getArgs().get(0);
        return Criteria.where(condition.getCol()).nin(args.toArray());
    }

}
