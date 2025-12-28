package cn.qingweico.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.qingweico.convert.StringConvert;
import cn.qingweico.encrypt.SM2Util;
import cn.qingweico.model.ApiResponse;
import cn.qingweico.service.LoginPasswordDecoder;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/decode")
    public ApiResponse<String> decodePassword(@RequestBody String content) {
        log.info("sm2加密数据 {}", content);
        String secretMode = "SM2";
        if (StringUtil.isNotEmpty(secretMode)) {
            LoginPasswordDecoder decoder = decoders.stream().filter(p -> p.support(secretMode)).findFirst().orElse(null);
            if (decoder == null) {
                return new ApiResponse<>("不支持当前加密模式");
            }
            JSONObject jsonObject = JSONUtil.parseObj(content);
            content = decoder.decode(StringConvert.toString(jsonObject.get("content")));
            log.info("sm2解密成功, 解密后的数据为 : {}", content);
        }
        return ApiResponse.ok(content);
    }

    public static void main(String[] args) {
        byte[] encrypt = SM2Util.encrypt(
                """
                        042d36dfdf646d71ff4380a87c653448905f02a5d6642b29c87a3fa5d63265d56c6152da1b8aa486a1b220d4138caf83ba4b69bd7da96da16b144386ff96ab57ca
                        """.trim(), "admin".getBytes());
        String encryptedHex = Hex.toHexString(encrypt);
        System.out.println(encryptedHex);
        System.out.println(new String(Hex.decode(SM2Util.decrypt("""
                2a8827a3471131059f83980ba1eca1e7f20a02af717d9523ea9c58f69ae3565b
                """.trim(), encryptedHex))));
    }
}
