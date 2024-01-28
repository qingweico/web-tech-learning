package cn.qingweico.alifacerecognition.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zqw
 * @date 2024/1/25
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {
    private static final long serialVersionUID = 8059511295718875912L;
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 人脸入库图片信息, 该信息保存到mongoDB的gridFS中, 为空string则表示未开启人脸登录
     */
    private String faceId;
}
