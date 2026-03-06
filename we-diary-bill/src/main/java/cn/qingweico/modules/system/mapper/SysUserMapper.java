package cn.qingweico.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qingweico.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);

	/**
	 *  根据部门Id查询用户信息
	 * @param page
	 * @param departId
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);
	/**
	 *  根据部门Id查询用户信息
	 * @param username
	 * @param departId
	 * @return
	 */
	List<SysUser> getUserByDepId(@Param("departId") String departId, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 * @param page
	 * @param
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);

	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param departId
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode);

	/**
	 * 根据手机号查询用户信息
	 * @param phone
	 * @return
	 */
	public SysUser getUserByPhone(@Param("phone") String phone);


	/**
	 * 根据邮箱查询用户信息
	 * @param email
	 * @return
	 */
	public SysUser getUserByEmail(@Param("email") String email);

	/*** 功能描述: 查询管理员与岗亭人员的信息
	 * @Author: lcy
	 * @Date: 2021/11/29
	 */
	List<SysUser> getGangTing();

	/**
	 * 获取交接班操作员列表
	 *
	 * @return 交接班操作员列表
	 */
	List<SysUser> getHandOverUserList();

}
