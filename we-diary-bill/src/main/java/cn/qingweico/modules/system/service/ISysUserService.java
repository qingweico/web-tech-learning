package cn.qingweico.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.common.api.vo.Result;
import cn.qingweico.modules.system.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

	SysUser getUserByName(String username);

	/**
	 * 添加用户和用户角色关系
	 *
	 * @param user
	 * @param roles
	 */
	void addUserWithRole(SysUser user, String roles);

	/**
	 * 修改用户和用户角色关系
	 *
	 * @param user
	 * @param roles
	 */
	void editUserWithRole(SysUser user, String roles);

	/**
	 * 获取用户的授权角色
	 *
	 * @param username
	 * @return
	 */
	List<String> getRole(String username);

	/**
	 * 根据部门Id查询
	 *
	 * @param page
	 * @param departId
	 * @param username
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

	/**
	 * 根据角色Id查询
	 *
	 * @param page
	 * @param roleId
	 * @param username
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String username);

	/**
	 * 根据用户名设置部门ID
	 *
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(String username, String orgCode);

	/**
	 * 根据手机号获取用户名和密码
	 *
	 * @param phone
	 * @return
	 */
	SysUser getUserByPhone(String phone);

	/**
	 * 根据邮箱获取用户
	 *
	 * @param email
	 * @return
	 */
	SysUser getUserByEmail(String email);

	/**
	 * 校验用户是否有效
	 *
	 * @param sysUser
	 * @return
	 */
	Result<JSONObject> checkUserIsEffective(SysUser sysUser);

	/**
	 * 校验用户是否有效
	 *
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective4Mobile(SysUser sysUser);

	/*** 功能描述: 所有岗亭的账户名
	 * @Author: lcy
	 * @Date: 2021/11/8
	 */
	List<SysUser> getGangTing();

	/**
	 * 获取交接班操作员列表
	 *
	 * @return 交接班操作员列表
	 */
	List<SysUser> getHandOverUserList();
}
