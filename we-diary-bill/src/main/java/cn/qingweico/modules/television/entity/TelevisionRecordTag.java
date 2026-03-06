package cn.qingweico.modules.television.entity;

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
 * 追剧记录标签关联实体类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Data
@TableName("television_record_tag")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TelevisionRecordTag对象", description = "追剧记录标签关联")
public class TelevisionRecordTag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法）
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 追剧记录ID
     */
    @ApiModelProperty(value = "追剧记录ID", required = true)
    private String recordId;

    /**
     * 标签ID
     */
    @ApiModelProperty(value = "标签ID", required = true)
    private String tagId;

    /**
     * 创建时间
     */
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







