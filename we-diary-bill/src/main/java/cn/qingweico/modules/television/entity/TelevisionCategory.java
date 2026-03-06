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
import java.util.Date;

/**
 * 观影分类实体类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Data
@TableName("television_category")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TelevisionCategory对象", description = "观影分类")
public class TelevisionCategory implements Serializable {

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
    @TableField("name")
    @Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称", required = true)
    private String name;

    /**
     * 排序
     */
    @TableField("sort")
    @ApiModelProperty(value = "排序")
    private Integer sort;

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


