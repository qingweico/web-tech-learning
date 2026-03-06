package cn.qingweico.common.util;

import com.alibaba.fastjson.JSON;
import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import cn.qingweico.common.api.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.MultiValueMap;

/**
 * @author : jdd孙庆伟
 * @date : Created in 2021/5/26 16:09
 * @description: guava的重试机制guava-retrying保存入/出场纪录
 * @modified By: `
 * @version: 1.0
 */
@Slf4j
@Async
public class GuavaRetryingUtils {

    public static Object retrySend(Object Object, String url, RestTemplateUtils restTemplateUtils, String appId, String appScrect) {
        int retryCount = 2;
        int interval = 1;

        ResultVo result = new ResultVo();
        try {
            for (int i = 0; i < retryCount; i++) {


                // 请求头设置,x-www-form-urlencoded格式的数据
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                //封装参数
                MultiValueMap<String, String> stringStringSortedMap = EncapsulationSignDataUtils.encapsulationParameters(Object, appId, appScrect);

                // 组装请求体
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(stringStringSortedMap, headers);

                //使用restTemplate远程调用云端保存接口
                ResponseEntity<ResultVo> cmsResultDTO = restTemplateUtils.post(url, request, ResultVo.class);

                //云端返回接口保存结果
                result = cmsResultDTO.getBody();
                log.info("云端保存接口调用结果:{}", result);

                //如果云端Code返回200(成功), 并且msg不等于"异常:主键冲突记录将不再保存
                if (result.getCode() == 200) {
                    break;
                }

                //间隔一秒重试
                Thread.sleep(interval * 1000);
                log.info("云端保存接口调用重试返回结果为:{}", result);
            }
        } catch (Exception e) {
            log.info("云端保存接口调用报错:{}", e.getMessage());
        }
        return result.getCode();

        /*    };*/
        //定义重试的机制原理
  /*      Retryer<ResultVo> smsRetryer = RetryerBuilder.<ResultVo>newBuilder()
                .retryIfResult(cmsResultDTO -> cmsResultDTO.getCode() != 200 && cmsResultDTO.getMsg().equals("异常:主键冲突记录将不再保存"))  // 返回的Code不是200要重试
                .retryIfResult(Predicates.<ResultVo>isNull())            // 返回的数据是null要重试
                .retryIfExceptionOfType(Exception.class)                    // 返回的异常错误类
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))//重试三次
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))    // 隔1秒重试
                .withRetryListener(new SMRetryListener<>()) //监听器
                .build();
        ResultVo cmsResultDTO = null;
        try {
            cmsResultDTO = smsRetryer.call(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cmsResultDTO.getCode();*/
    }

    /**
     * 发送post请求到云端
     *
     * @param Object
     * @param url
     * @param restTemplateUtils
     * @param appId
     * @param appScrect
     * @return cn.qingweico.common.api.vo.ResultVo
     * @author jdd孙庆伟
     * @date 2021/6/25 11:41:35
     * @version 1.0
     */
    public static ResultVo restTemplatePosts(Object Object, String url, RestTemplateUtils restTemplateUtils, String appId, String appScrect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //封装参数
        MultiValueMap<String, String> stringStringSortedMap = EncapsulationSignDataUtils.encapsulationParameters(Object, appId, appScrect);
        // 组装请求体
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(stringStringSortedMap, headers);
        ResponseEntity<ResultVo> posts = restTemplateUtils.post(url, request, ResultVo.class);
        ResultVo body = posts.getBody();
        return body;
    }


    /**
     * 重试监听器
     *
     * @author jdd孙庆伟
     * @version 1.0
     * @return
     * @date 2021/5/25 15:53:16
     */
    public static class SMRetryListener<V> implements RetryListener {
        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            log.info("[retry]time=" + attempt.getAttemptNumber());
            if (attempt.hasException()) {
                log.error("retry exception", attempt.getExceptionCause());
            }
            if (attempt.hasResult()) {
                if (attempt.getResult() == null) {
                    log.info("云端保存接口调用重试返回结果为空");
                } else {
                    log.info("云端保存接口调用重试返回结果为:{}", JSON.toJSONString(attempt.getResult()));
                }
            }
        }
    }
}
