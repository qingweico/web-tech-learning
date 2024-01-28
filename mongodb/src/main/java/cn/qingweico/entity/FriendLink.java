package cn.qingweico.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author zqw
 * @date 2022/4/2
 */
@Data
@Document("FriendLink")
public class FriendLink {
    @Id
    private String id;
    @Field("name")
    private String name;
    @Field("url")
    private String url;
    @Field("avl")
    private String avl;
    @Field("create")
    private Date create;
    @Field("update")
    private Date update;
}
