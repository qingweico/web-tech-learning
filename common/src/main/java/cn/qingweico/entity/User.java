package cn.qingweico.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zqw
 * @date 2023/11/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 7373097476584926935L;
    private String id;
    private String username;
    private String address;
    private Date birthday;
    private String mobile;
}
