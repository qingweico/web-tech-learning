package cn.qingweico.controller;

import cn.hutool.json.JSONUtil;
import cn.qingweico.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zqw
 * @date 2024/5/11
 */
@RestController
@RequestMapping
public class UserController {

    @PostMapping("/user")
    public String postUser(@RequestBody User user) {
        return JSONUtil.toJsonStr(user);
    }
}
