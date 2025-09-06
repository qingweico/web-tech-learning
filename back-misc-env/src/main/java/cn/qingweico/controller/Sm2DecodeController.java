package cn.qingweico.controller;

import cn.qingweico.model.ApiResponse;
import cn.qingweico.service.LoginPasswordDecoder;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zqw
 * @date 2025/9/6
 */
@Slf4j
@RestController
@RequestMapping("/sm2")
public class Sm2DecodeController {
    @Resource
    private List<LoginPasswordDecoder> decoders;

    @GetMapping("/decode")
    public ApiResponse<String> decodePassword(String content) {
        log.info("sm2加密数据 {}", content);
        String secretMode = "SM2";
        if (StringUtil.isNotEmpty(secretMode)) {
            LoginPasswordDecoder decoder = decoders.stream().filter(p -> p.support(secretMode)).findFirst().orElse(null);
            if (decoder == null) {
                return new ApiResponse<>("不支持当前加密模式");
            }
            content = decoder.decode(content);
            log.info("sm2解密成功, 解密后的数据为 : {}", content);
        }
        return ApiResponse.ok();
    }

}
