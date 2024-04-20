package cn.qingweico.model;

import lombok.*;

import java.time.Instant;

/**
 * API 响应 JSON 格式数据
 *
 * @author zqw
 * @date 2023/10/12
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private @Builder.Default String code = "200";
    private @Builder.Default String msg = "OK";
    private @Builder.Default boolean success = false;
    private @Builder.Default Long now = Instant.now().getEpochSecond();
    private Object data;

    public static ApiResponse ok(Object data) {
        return ApiResponse.builder()
                .success(true)
                .data(data)
                .build();
    }

    public static ApiResponse ok() {
        return ApiResponse.builder()
                .success(true)
                .build();
    }
    public static ApiResponse error(String msg) {
        return ApiResponse.builder()
                .msg(msg)
                .success(false)
                .build();
    }
    public static ApiResponse success(String msg) {
        return ApiResponse.builder()
                .msg(msg)
                .success(true)
                .build();
    }
}
