package cn.qingweico.afr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName(value = "t_user")
@Accessors(chain = true)
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 8059511295718875912L;
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户名
     */
    @TableField
    private String username;
    /**
     * 人脸入库图片信息, 该信息保存到mongoDB的gridFS中, 为空string则表示未开启人脸登录
     */
    @TableField
    private String faceId;
    @TableField
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @TableField
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
