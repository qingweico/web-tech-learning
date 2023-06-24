package cn.qingweico.entity;

import lombok.*;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zqw
 * @date 2023/6/21
 */
@Data
@Builder
@Accessors
@Table(name = "tb_goods")
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    @Id
    private Integer id;
    private String name;
    private String no;
    private BigDecimal unitPrice;
    private BigDecimal inventory;
    private String remark;
    private Integer available;
}
