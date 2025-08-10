package cn.qingweico.entity;

import cn.qingweico.validate.anno.*;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Data
@TableName("t_bill")
public class Bill {
    @Required
    @Length(min = 6, max = 20)
    private String orderNo;

    @Mobile
    private String phone;

    @Min(value = 0.01)
    @Max(value = 1000000L)
    private BigDecimal amount;

    @InEnum(BillStatus.class)
    private String status;

    @In({"ALIPAY", "WECHAT", "BANK"})
    private String paymentMethod;

    @Getter
    public enum BillStatus {
        UNPAID("unpaid"), PAID("paid"), REFUNDED("refunded"), CANCELLED("cancelled");

        private final String code;

        BillStatus(String code) {
            this.code = code;
        }
    }

}
