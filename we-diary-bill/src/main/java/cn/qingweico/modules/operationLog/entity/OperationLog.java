package cn.qingweico.modules.operationLog.entity;

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

import java.util.Date;

/**
 * @author zqw
 */
@Data
@TableName("operation_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "operation_log对象", description = "操作日志")
public class OperationLog {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR) 
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 日志等级
     */
    @Excel(name = "日志等级", width = 15)
    @ApiModelProperty(value = "日志等级")
    private Integer level;
    /**
     * 被操作的对象
     */
    @Excel(name = "被操作的对象", width = 15)
    @ApiModelProperty(value = "被操作的对象")
    private String operationUnit;
    /**
     * 方法名称
     */
    @Excel(name = "方法名称", width = 15)
    @ApiModelProperty(value = "方法名称")
    private String method;
    /**
     * 方法参数
     */
    @Excel(name = "方法参数", width = 15)
    @ApiModelProperty(value = "方法参数")
    private Object args;
    /**
     * 操作人
     */
    @Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    private String username;
    /**
     * 日志描述
     */
    @Excel(name = "日志描述", width = 15)
    @ApiModelProperty(value = "日志描述")
    @TableField("`describe`")
    private String describe;
    /**
     * 操作类型
     */
    @Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    private String operationType;
    /**
     * 方法运行时间
     */
    @Excel(name = "方法运行时间", width = 15)
    @ApiModelProperty(value = "方法运行时间")
    private Integer runTime;
    /**
     * 方法返回值
     */
    @Excel(name = "方法返回值", width = 15)
    @ApiModelProperty(value = "方法返回值")
    private Object returnValue;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date created;
    /**
     * 异常信息
     */
    @Excel(name = "异常信息", width = 15)
    @ApiModelProperty(value = "异常信息")
    private Object exceptionMsg;
    /**
     * 异常代码
     */
    @Excel(name = "异常代码", width = 15)
    @ApiModelProperty(value = "异常代码")
    private Object exceptionCode;
    /**
     * IP
     */
    @Excel(name = "IP", width = 15)
    @ApiModelProperty(value = "IP")
    private String ip;

}
