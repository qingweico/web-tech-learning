package cn.qingweico.entity.model;

import lombok.Data;

/**
 * @author zqw
 * @date 2023/11/5
 */
@Data
public class BookModel {
    private String id;
    private String name;
    private Double price;
    private Integer pages;
    private String createTime;
    private String updateTime;
    private Integer pageNo = 1;
    private Integer pageSize = 20;
}
