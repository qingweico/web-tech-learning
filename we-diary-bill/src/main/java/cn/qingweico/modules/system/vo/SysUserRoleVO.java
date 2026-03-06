package cn.qingweico.modules.system.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhouqingwei
 */
@Data
public class SysUserRoleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    private String roleId;
    /**
     * 对应的用户id集合
     */
    private List<String> userIdList;

    public SysUserRoleVO() {

    }

    public SysUserRoleVO(String roleId, List<String> userIdList) {
        super();
        this.roleId = roleId;
        this.userIdList = userIdList;
    }


}
