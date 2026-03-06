package cn.qingweico.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.constant.CacheConstant;
import cn.qingweico.common.constant.CommonConstant;
import cn.qingweico.common.system.api.ISysBaseAPI;
import cn.qingweico.common.util.oConvertUtils;
import cn.qingweico.modules.system.entity.SysPermission;
import cn.qingweico.modules.system.entity.SysUser;
import cn.qingweico.modules.system.entity.SysUserRole;
import cn.qingweico.modules.system.mapper.SysPermissionMapper;
import cn.qingweico.modules.system.mapper.SysUserMapper;
import cn.qingweico.modules.system.mapper.SysUserRoleMapper;
import cn.qingweico.modules.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhouqingwei
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private ISysBaseAPI sysBaseAPI;

    @Override
    public SysUser getUserByName(String username) {
        return userMapper.getUserByName(username);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserWithRole(SysUser user, String roles) {
        this.save(user);
        if (oConvertUtils.isNotEmpty(roles)) {
            SysUserRole userRole = new SysUserRole(user.getId(), roles);
            sysUserRoleMapper.insert(userRole);
        }
    }

    @Override
    @CacheEvict(value = CacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void editUserWithRole(SysUser user, String roles) {
        this.updateById(user);
        //先删后加
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        if (oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }


    @Override
    public List<String> getRole(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    @Override
    @Cacheable(value = CacheConstant.LOGIN_USER_RULES_CACHE, key = "'Roles_'+#username")
    public Set<String> getUserRolesSet(String username) {
        // 查询用户拥有的角色集合
        List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
        log.info("-------通过数据库读取用户拥有的角色Rules------username:  " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
        return new HashSet<>(roles);
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    @Override
    @Cacheable(value = CacheConstant.LOGIN_USER_RULES_CACHE, key = "'Permissions_'+#username")
    public Set<String> getUserPermissionsSet(String username) {
        Set<String> permissionSet = new HashSet<>();
        List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
        for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
            if (oConvertUtils.isNotEmpty(po.getPerms())) {
                permissionSet.add(po.getPerms());
            }
        }
        log.info("-------通过数据库读取用户拥有的权限Perms------username:  " + username + ",Perms size: " + (permissionSet == null ? 0 : permissionSet.size()));
        return permissionSet;
    }


    /**
     * 根据部门Id查询
     *
     * @param page
     * @param departId
     * @param username
     * @return
     */
    @Override
    public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username) {
        return userMapper.getUserByDepId(page, departId, username);
    }


    /**
     * 根据角色Id查询
     *
     * @param page
     * @param roleId
     * @param username
     * @return
     */
    @Override
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
        return userMapper.getUserByRoleId(page, roleId, username);
    }


    @Override
    public void updateUserDepart(String username, String orgCode) {
        baseMapper.updateUserDepart(username, orgCode);
    }


    @Override
    public SysUser getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }


    @Override
    public SysUser getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }


    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<JSONObject> checkUserIsEffective(SysUser sysUser) {
        Result<JSONObject> result = new Result<>();
        //情况1: 根据用户信息查询, 该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在, 请注册");
            sysBaseAPI.addLog("用户登录失败, 用户不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }
        //情况2: 根据用户信息查询, 该用户已注销
        if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
            sysBaseAPI.addLog("用户登录失败, 用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已注销");
            return result;
        }
        //情况3: 根据用户信息查询, 该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
            sysBaseAPI.addLog("用户登录失败, 用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective4Mobile(SysUser sysUser) {
        Result<?> result = new Result<>();
        //情况1: 根据用户信息查询, 该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在, 请注册");
            sysBaseAPI.addLog("用户登录失败, 用户不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }
        //情况2: 根据用户信息查询, 该用户已注销
        if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
            sysBaseAPI.addLog("用户登录失败, 用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已注销");
            return result;
        }
        //情况3: 根据用户信息查询, 该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
            sysBaseAPI.addLog("用户登录失败, 用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已冻结");
            return result;
        }

        //情况4: 根据用户信息查询, 该用户有手持端权限
        if (CommonConstant.SIGN_MOBILE_NONE.equals(sysUser.getSignMobile())) {
            sysBaseAPI.addLog("用户登录失败, 用户名:" + sysUser.getUsername() + "无手持端权限！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户无手持端权限");
            return result;
        }
        return result;
    }

    @Override
    public List<SysUser> getGangTing() {
        return baseMapper.getGangTing();
    }

    /**
     * 获取交接班操作员列表
     *
     * @return 交接班操作员列表
     */
    @Override
    public List<SysUser> getHandOverUserList() {
        return baseMapper.getHandOverUserList();
    }
}
