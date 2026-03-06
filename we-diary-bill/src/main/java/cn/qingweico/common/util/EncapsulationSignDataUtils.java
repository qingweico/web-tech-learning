package cn.qingweico.common.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author : jdd孙庆伟
 * @date : Created in 2021/5/26 16:11
 * @description: 封装restTemplate参数工具类
 * @modified By: `
 * @version: 1.0
 */
public class EncapsulationSignDataUtils {

    public static MultiValueMap<String, String> encapsulationParameters(Object object, String appId, String appScrect) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        try {
            //封装参数 获取签名后参数map
            SortedMap<String, String> returnMap = new TreeMap<>();
            returnMap.putAll(parseObject(toJSONString(object), Map.class));
            AppIdSecretUtil.getParamsMapAfterSign(returnMap, appId, appScrect);
            for (Map.Entry<String, String> entry : returnMap.entrySet()) {
                multiValueMap.put(entry.getKey(), Collections.singletonList(String.valueOf(entry.getValue())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multiValueMap;
    }


}
