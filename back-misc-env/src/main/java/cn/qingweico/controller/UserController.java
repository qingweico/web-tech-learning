package cn.qingweico.controller;

import cn.qingweico.annotation.SemaphoreLimit;
import cn.qingweico.concurrent.UnsafeSupport;
import cn.qingweico.entity.dto.ResponseDto;
import cn.qingweico.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


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

    @RequestMapping(value = "sl", method = RequestMethod.GET)
    @SemaphoreLimit(name = "user", maxConcurrent = 1, ttl = 10)
    public ApiResponse<?> semaphoreLimit() {
        UnsafeSupport.shortWait(300000);
        return ApiResponse.ok();
    }

    @PostMapping(value = "/urlencoded", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResponse<String> urlencoded(
            @RequestParam String username) {
        return ApiResponse.ok(username);
    }

    @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> multipart(
            @RequestParam String username,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            log.info("File: {}, Size: {}", fileName, fileSize);
        }
        return ApiResponse.ok(username);
    }
    /*@RequestBody 用于将 HTTP 请求体中的 JSON/XML 数据绑定到 Java 对象上;因为每个 HTTP 请求只有一个请求体, 所以只能有一个被 @RequestBody 修饰的参数;*/
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> json(
            @RequestBody Map<String, Object> params) {
        return ApiResponse.ok(params);
    }

}
