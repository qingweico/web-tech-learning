package cn.qingweico.modules.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 账单记录查询VO
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Data
@ApiModel(value = "BillRecordQueryVO对象", description = "账单记录查询VO")
public class BillRecordQueryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "账单类型：1-收入，2-支出")
    private Integer billType;

    @ApiModelProperty(value = "分类ID")
    private String categoryId;

    @ApiModelProperty(value = "账户ID")
    private String accountId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}


