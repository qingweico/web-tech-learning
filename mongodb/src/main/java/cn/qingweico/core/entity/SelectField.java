package cn.qingweico.core.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询的列
 *
 * @author zqw
 * @date 2023/11/04
 */
@Setter
@Getter
public class SelectField {

    /**
     * 集合对应的列
     */
    private String col;

    public SelectField(String col) {
        this.col = col;
    }
}
