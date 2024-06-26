package cn.qingweico.model;

import lombok.Data;

import java.util.List;

/**
 * 用来返回分页的数据格式
 *
 * @author zqw
 * @date 2021/9/9
 */
@Data
public class PagedResult {
    /**
     * 当前页数
     */
    private long currentPage;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 总记录数
     */
    private long totalNumber;
    /**
     * 每行显示的内容
     */
    private List<?> rows;
}
