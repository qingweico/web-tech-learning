package cn.qingweico.controller;

import cn.qingweico.config.ApiGroup;
import cn.qingweico.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zqw
 * @date 2023/11/11
 */
@RestController
@RequestMapping("/user")
@ApiGroup("sysUser")
@ApiOperation("系统用户接口")
public class SysUserController {

    static SysUser sysUser = new SysUser();

    static {
        sysUser.setMobile("17796706221");
        sysUser.setEmail("17796706221@163.com");
    }

    @ApiOperation("根据用户名查询系统用户")
    @GetMapping("getSysUser")
    public SysUser getSysUser(@ApiParam("用户名") @RequestParam String username) {
        sysUser.setUsername(username);
        return sysUser;
    }

    @GetMapping("helloWorld")
    public String helloWorld() {
        return "Hello World";
    }
}
