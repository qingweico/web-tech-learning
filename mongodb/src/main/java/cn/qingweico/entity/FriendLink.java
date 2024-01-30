package cn.qingweico.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @see DateTimeFormat 前端请求后端接口时, 将字符串的日期格式化为日期类型
 * @see JsonFormat 后端向前端响应JSON数据时将日期类型按照pattern转换为JSON字符串
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
    //@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create;
    @Field("update")
    //@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update;
}
