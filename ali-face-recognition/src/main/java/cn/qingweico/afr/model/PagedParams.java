package cn.qingweico.afr.model;

import lombok.Data;

/**
 * @author zqw
 * @date 2024/1/30
 */
@Data
public class PagedParams {
    private Integer page = 1;
    private Integer pageSize = 10;
}
