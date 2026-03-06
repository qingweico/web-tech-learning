package cn.qingweico.modules.television.entity;

import cn.qingweico.common.util.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.List;

/**
 * 追剧记录实体类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Data
@TableName("television_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TelevisionRecord对象", description = "追剧记录")
public class TelevisionRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法）
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 剧名
     */
    @Excel(name = "剧名", width = 20)
    @ApiModelProperty(value = "剧名", required = true)
    private String name;

    /**
     * 集数
     */
    @Excel(name = "集数", width = 10)
    @ApiModelProperty(value = "集数")
    private Integer episode;

    /**
     * 观看状态：0-未开始，1-正在追，2-已结束
     */
    @Excel(name = "观看状态", width = 15, dicCode = "watch_status")
    @ApiModelProperty(value = "观看状态：0-未开始，1-正在追，2-已结束", required = true)
    private Integer watchStatus;

    /**
     * 评分(1-10分，带有一位小数点)
     */
    @Excel(name = "评分", width = 10)
    @ApiModelProperty(value = "评分(1-10分，带有一位小数点)")
    private BigDecimal rating;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private String categoryId;

    /**
     * 开始观看时间
     */
    @Excel(name = "开始观看时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始观看时间")
    private Date startTime;

    /**
     * 结束观看时间
     */
    @Excel(name = "结束观看时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束观看时间")
    private Date endTime;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 30)
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

    /**
     * 标签ID列表（用于前端传参，不映射到数据库）
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "标签ID列表")
    private List<String> tagIds;

    /**
     * 分类名称（用于查询结果展示，不映射到数据库）
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 标签名称列表（用于查询结果展示，不映射到数据库）
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "标签名称列表")
    private List<String> tagNames;
}

