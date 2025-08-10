package cn.qingweico.entity.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author zqw
 * @date 2025/7/10
 */
@Data
public class ResponseDto {
    private String msg;
    private String code;
    private Map<String, Object> data;
}
