package cn.qingweico.controller;

import cn.qingweico.entity.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


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

    /**
     * {
     *     "msg": "OK",
     *     "code": 200,
     *     "data": {
     *         "accountBill": {
     *             "money": "100",
     *             "amount": "24",
     *             "rate": 12
     *         },
     *         "plInfo": {
     *             "name": "中国建设银行",
     *             "date": "2025-08-12",
     *             "sign": "Y"
     *         }
     *     }
     * }
     */
    @PostMapping(value = "postParamsAnalysis")
    public void postParamsAnalysis(@RequestBody ResponseDto responseDto) {
        log.info("{}", responseDto);
    }
}
