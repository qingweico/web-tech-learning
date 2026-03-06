package cn.qingweico.modules.system.service;

import cn.qingweico.model.BusinessException;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.modules.system.entity.SysPermission;
import cn.qingweico.modules.system.model.TreeModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<TreeModel> queryListByParentId(String parentId);

    /**
     * 真实删除
     *
     * @param id
     * @throws BusinessException
     */
    void deletePermission(String id) throws BusinessException ;

    /**
     * 逻辑删除
     *
     * @param id
     * @throws BusinessException
     */
    void deletePermissionLogical(String id) throws BusinessException ;

    void addPermission(SysPermission sysPermission) throws BusinessException ;

    void editPermission(SysPermission sysPermission) throws BusinessException ;

    List<SysPermission> queryByUser(String username);

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     *
     * @param id
     * @return
     */
    void deletePermRuleByPermId(String id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     *
     * @return
     */
    List<String> queryPermissionUrlWithStar();
}
