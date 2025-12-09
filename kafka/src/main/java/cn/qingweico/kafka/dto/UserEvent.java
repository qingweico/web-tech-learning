package cn.qingweico.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zqw
 * @date 2025/12/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String userId;
    private String username;
    private UserAction action;
    private String details;
    private LocalDateTime timestamp;

    public enum UserAction {
        REGISTER, LOGIN, LOGOUT, UPDATE_PROFILE, DELETE_ACCOUNT
    }
}
