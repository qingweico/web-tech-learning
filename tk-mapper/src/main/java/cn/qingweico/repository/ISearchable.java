package cn.qingweico.repository;

import cn.qingweico.entity.BaseEntity;

import java.util.List;

/**
 * @author zqw
 * @date 2025/7/18
 */
public interface ISearchable<T extends BaseEntity, TExample extends IQueryExample<T, ?>> {

    /*根据条件和列名查询数据*/
    List<T> list(TExample example, String... columns);
}
