package cn.qingweico.modules.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouqingwei
 */
@Data
@ApiModel(value = "登录对象", description = "登录对象")
public class SysLoginModel {
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

}
