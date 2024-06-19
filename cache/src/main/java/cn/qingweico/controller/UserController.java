package cn.qingweico.controller;

import cn.hutool.json.JSONUtil;
import cn.qingweico.client.RedisClient;
import cn.qingweico.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2024/5/11
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private RedisClient redisClient;
    @PostMapping("/post")
    public String postUser(@RequestBody User user) {
        return JSONUtil.toJsonStr(user);
    }
    @GetMapping("/get")
    public String getUser() {
        User user = redisClient.get("User", User.class);
        if(user == null) {
            user = User.builder()
                    .id("1")
                    .username("bob_jones")
                    .address("789 Oak St, Gotham")
                    .mobile("555-9012")
                    .build();
            redisClient.set("User", user);
        }
        return JSONUtil.toJsonStr(user);
    }
}
