package cn.qingweico.controller;

import cn.qingweico.annotation.AccessInterceptor;
import cn.qingweico.enums.InterceptAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zqw
 * @date 2024/6/22
 */
@Slf4j
@RestController
public class LoginController {



    @AccessInterceptor(key = "fingerprint", fallbackMethod = "loginErr", permitsPerSecond = 1.0d, blacklistCount = 10, type = InterceptAction.CUSTOMER)
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(String fingerprint) {
        return "登录成功 " + fingerprint;
    }

    public String loginErr(String fingerprint) {
        return "登录频次限制 : " + fingerprint;
    }


}
