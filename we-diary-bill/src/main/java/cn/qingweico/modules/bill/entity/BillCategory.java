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
import java.util.Date;

/**
 * 账单分类实体类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Data
@TableName("bill_category")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BillCategory对象", description = "账单分类")
public class BillCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法）
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称", required = true)
    private String categoryName;

    /**
     * 分类类型：1-收入，2-支出
     */
    @Excel(name = "分类类型", width = 15, dicCode = "bill_type")
    @ApiModelProperty(value = "分类类型：1-收入，2-支出", required = true)
    private Integer categoryType;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

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


