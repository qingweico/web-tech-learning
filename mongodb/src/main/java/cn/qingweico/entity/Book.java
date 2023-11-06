package cn.qingweico.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author zqw
 * @date 2023/11/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private String id;
    private String name;
    private Double price;
    private Integer pages;
    private String createTime;
    private String updateTime;
}
