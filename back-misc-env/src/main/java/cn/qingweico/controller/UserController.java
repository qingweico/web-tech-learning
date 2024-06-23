package cn.qingweico.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zqw
 * @date 2024/6/22
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger businessLog = LoggerFactory.getLogger("businessLog");

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public void get() {
        log.info("GetUser");
    }

    @RequestMapping(value = "businessLog", method = RequestMethod.GET)
    public void businessLog() {
        businessLog.info("这是用来记录业务操作日志");
    }
}
