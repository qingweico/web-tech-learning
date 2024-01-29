package cn.qingweico.elasticlowerclient.model;

import lombok.Data;

/**
 * @author zqw
 * @date 2024/1/28
 */
@Data
public class ReqParams {
    private String name;
    private String phone;
    private String address;
    private Integer page = 1;
    private Integer pageSize = 10;
}
