package cn.qingweico.common.util;

import cn.qingweico.common.api.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Description:
 * @author:  jdd尚岳
 * @Date: 2022-01-08 15:55
 */
@Component
@Slf4j
public class MonthlyUploadHttpUtils {
    @Resource
    RestTemplateUtils  restTemplateUtils;

    /**
     * 通用异步上传接口
     * @param url
     * @param entity
     * @param <T>
     */

    public  <T> Result postMonthlyUpload(String url,T entity){
        Result body=null;
        try {
            ResponseEntity<Result> post = restTemplateUtils.post(url, entity, Result.class);
            body = post.getBody();
            log.info("向云端同步数据!-》{}",body.getMessage());
        }catch (Exception e){
            log.error("向云端同步数据异常!->{}",e.getMessage());
        }
        return body;
    }













}
