package cn.qingweico.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * @author zqw
 * @date 2023/6/21
 */
@Data
@Builder
@Accessors
@TableName(value = "tb_goods")
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     *  TableField 和 TableId 都是来解决数据库字段和实体属性不一致问题(默认会将下划线转成驼峰)
     */
    @TableField
    private String name;
    private String no;
    private BigDecimal unitPrice;
    private BigDecimal inventory;
    private String remark;
    @TableLogic
    private Integer available;
    @Version
    private Integer version;
}
