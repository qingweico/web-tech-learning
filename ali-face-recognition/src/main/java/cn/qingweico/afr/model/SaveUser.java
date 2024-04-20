package cn.qingweico.afr.model;


import lombok.Data;

/**
 * @author zqw
 * @date 2022/4/9
 */
@Data
public class SaveUser {
    private String id;
    private String faceId;
    private String username;
    private Boolean enableFace;
}
