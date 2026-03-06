package cn.qingweico.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class JddPage<T> extends Page<T> {
    /**
     * 统计sql, 当分页sql查询字段很多时, 手动写countsql, 避免count很慢
     */
    private String countSqlId;

    public JddPage(Page page) {
        super(page.getCurrent(), page.getSize());
    }
}
