package cn.qingweico.repository;

import cn.qingweico.entity.BaseEntity;

import java.util.List;

/**
 * @author zqw
 * @date 2025/7/18
 */
public interface IQueryExample<T extends BaseEntity, TCriteria> {
    void setOrderByClause(String orderByClause);

    String getOrderByClause();

    void setDistinct(boolean distinct);

    boolean isDistinct();

    List<TCriteria> getOrderCriteria();

    void or(TCriteria criteria);

    TCriteria or();

    TCriteria createCriteria();

    void clear();
}

