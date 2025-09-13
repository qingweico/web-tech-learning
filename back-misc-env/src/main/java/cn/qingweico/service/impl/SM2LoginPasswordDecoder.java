package cn.qingweico.service.impl;

import cn.qingweico.encrypt.SM2Util;
import cn.qingweico.model.BusinessException;
import cn.qingweico.service.LoginPasswordDecoder;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author zqw
 * @date 2025/9/6
 */
@Component
public class SM2LoginPasswordDecoder implements LoginPasswordDecoder {

    @Value("${encrypt.sm2.private-key}")
    private String privateKey;
    @Value("${encrypt.sm2.public-key}")
    private String publicKey;

    @Override
    public boolean support(String mode) {
        return StringUtils.equals(mode, "SM2");
    }

    @Override
    public String decode(String secretPassword) {
        if (StringUtils.isAnyEmpty(privateKey, publicKey)) {
            return StringUtils.EMPTY;
        }
        try {
            if (publicKey.startsWith("04")) {
                return new String(Hex.decode(SM2Util.decryptWith04(privateKey, secretPassword)), StandardCharsets.UTF_8);

            } else {
                return new String(Hex.decode(SM2Util.decrypt(privateKey, secretPassword)), StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            throw new BusinessException("密码校验失败,密文算法错误", e);
        }
    }
}
