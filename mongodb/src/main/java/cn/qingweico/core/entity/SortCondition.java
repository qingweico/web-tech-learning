package cn.qingweico.core.entity;

import cn.qingweico.core.constant.SortType;
import lombok.Getter;
import lombok.Setter;

/**
 * 排序字段
 *
 * @author zqw
 * @date 2023/11/04
 */
@Setter
@Getter
public class SortCondition {

    /**
     * 排序类型
     */
    private SortType sortType;

    /**
     * 集合对应的列
     */
    private String col;


    public SortCondition(SortType sortType, String col) {
        this.sortType = sortType;
        this.col = col;
    }
}
