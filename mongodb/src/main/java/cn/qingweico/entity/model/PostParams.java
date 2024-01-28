package cn.qingweico.entity.model;

import lombok.Data;

/**
 * @author zqw
 * @date 2024/1/27
 */
@Data
public class PostParams {
    private String name;
    private String url;
    private String avl;
    private Integer page = 1;
    private Integer pageSize = 10;
}
