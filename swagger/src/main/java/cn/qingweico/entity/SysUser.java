package cn.qingweico.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zqw
 * @date 2023/11/11
 */
@Data
@ApiModel("系统用户表")
public class SysUser {
    @ApiModelProperty(value = "用户名", dataType = "String", required = true)
    private String username;
    @ApiModelProperty(value = "手机号", dataType = "String", required = true)
    private String mobile;
    @ApiModelProperty(value = "邮箱", dataType = "String")
    private String email;
}
