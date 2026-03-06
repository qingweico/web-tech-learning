package cn.qingweico.modules.bill.entity;

import cn.qingweico.common.util.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单记录实体类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Data
@TableName("bill_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BillRecord对象", description = "账单记录")
public class BillRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法）
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private String userId;

    /**
     * 金额
     */
    @Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;

    /**
     * 账单类型：1-收入，2-支出
     */
    @Excel(name = "账单类型", width = 15, dicCode = "bill_type")
    @ApiModelProperty(value = "账单类型：1-收入，2-支出", required = true)
    private Integer billType;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID", required = true)
    private String categoryId;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String payment;

    /**
     * 账单时间（可手动选择，不选则=创建时间）
     */
    @Excel(name = "账单时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "账单时间")
    private Date billTime;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 20)
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date created;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date lastUpd;

    /**
     * 创建者ID
     */
    @ApiModelProperty(value = "创建者ID")
    private String createdBy;

    /**
     * 修改者ID
     */
    @ApiModelProperty(value = "修改者ID")
    private String lastUpdBy;
}


