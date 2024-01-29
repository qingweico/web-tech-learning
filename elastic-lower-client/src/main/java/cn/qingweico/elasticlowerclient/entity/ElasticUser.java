package cn.qingweico.elasticlowerclient.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

/**
 * @author zqw
 * @date 2024/1/28
 */
@Data
@Document(indexName = "user", type = "_doc")
public class ElasticUser {
    @Id
    private String id;
    @Field
    private String name;
    @Field
    private String address;
    @Field
    private String phone;
    @Field
    private String avl;
    @Field(fielddata = true)
    private String gender;
    @Field(fielddata = true)
    private String province;
    @Field
    private Date create;
    @Field
    private Date update;
}
