package cn.qingweico.controller;

import cn.qingweico.annotation.SemaphoreLimit;
import cn.qingweico.concurrent.UnsafeSupport;
import cn.qingweico.entity.dto.ResponseDto;
import cn.qingweico.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    @SemaphoreLimit(name = "user")
    public ApiResponse<?> semaphoreLimit() {
        UnsafeSupport.shortWait(3000);
        return ApiResponse.ok();
    }

    @PostMapping(value = "/form-urlencoded", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<String> formUrlencoded(
            @RequestParam String username) {
        return ResponseEntity.ok(username);
    }

    @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<String> multipart(
            @RequestParam String username,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            log.info("File: {}, Size: {}", fileName, fileSize);
        }
        return ResponseEntity.ok(username);
    }

}
