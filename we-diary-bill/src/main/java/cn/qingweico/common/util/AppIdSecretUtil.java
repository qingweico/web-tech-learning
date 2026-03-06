package cn.qingweico.common.util;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.*;

/**
 * @Description AppIdSecretUtil
 * @Author wanglei
 * @Date 2021-05-24 19:26
 **/
@Slf4j
public class AppIdSecretUtil {

    public static String generateAppKey() {
        return UUID.randomUUID().toString();
    }

    public static String generateAppScrect() {
        return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()).toUpperCase();
    }

    /**
     * @Description 生成sign
     * @Author wanglei
     * @Date 2021-05-24 19:55
     * @Param [params, appScrect]
     * @version 1.0
     * @return java.lang.String
     **/
    public static String generateSign(SortedMap<String, String> params, String appScrect){
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> es =  params.entrySet();
        Iterator<Map.Entry<String,String>> it =  es.iterator();

        while (it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            if(StringUtils.isNotBlank(value) && !"sign".equals(key) && !"appScrect".equals(key)){
                sb.append(key + "=" + value + "&");
            }
        }

        sb.append("appScrect=").append(appScrect);
        String sign = MD5(sb.toString()).toUpperCase();
        return sign;
    }

    /**
     * @Description 获取签名后参数map
     * @Author wanglei
     * @Date 2021-05-25 16:23
     * @Param [params, appScrect]
     * @version 1.0
     * @return java.lang.String
     **/
    public static SortedMap<String, String> getParamsMapAfterSign(SortedMap<String, String> params, String appId, String appScrect){
        params.put("appId", appId);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("sign", generateSign(params, appScrect));
        return params;
    }

    /**
     * @Description 校验签名
     * @Author wanglei
     * @Date 2021-05-24 20:00
     * @Param [params, appScrect]
     * @version 1.0
     * @return boolean
     **/
    public static boolean checkSign(SortedMap<String, String> params, String appScrect){
        log.info("校验签名params: {}" , JSON.toJSONString(params));
        if(StringUtils.isBlank(params.get("sign"))){
            log.info("校验签名sign为空");
            return false;
        }
        if(StringUtils.isBlank(params.get("timestamp"))){
            log.info("校验签名timestamp为空");
            return false;
        }

        if(System.currentTimeMillis() - Long.valueOf(params.get("timestamp")) > 10*60*1000){
            log.info("校验签名请求超过10分钟");
            return false;
        }

        String sign = generateSign(params,appScrect);
        String paramSign = params.get("sign").toUpperCase();
        log.info("校验生成签名: {}, 客户端签名: {}", sign, paramSign);
        return paramSign.equals(sign);
    }


    /**
     * @Description 获取有序map
     * @Author wanglei
     * @Date 2021-05-24 19:39
     * @Param [map]
     * @version 1.0
     * @return java.util.SortedMap<java.lang.String,java.lang.String>
     **/
    public static SortedMap<String,String> getSortedMap(Map<String,String> map){
        SortedMap<String, String> sortedMap = new TreeMap<>();
        Iterator<String> it =  map.keySet().iterator();

        while (it.hasNext()){
            String key  = it.next();
            String value = map.get(key);
            String temp = "";
            if( null != value){
                temp = value.trim();
            }
            sortedMap.put(key,temp);
        }
        return sortedMap;
    }


    /**
     * @Description md5常用工具类
     * @Author wanglei
     * @Date 2021-05-24 19:38
     * @Param [data]
     * @version 1.0
     * @return java.lang.String
     **/
    public static String MD5(String data){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte [] array = md5.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
