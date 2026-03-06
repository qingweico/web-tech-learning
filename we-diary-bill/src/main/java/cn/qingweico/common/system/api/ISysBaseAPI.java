package cn.qingweico.common.system.api;

import cn.qingweico.common.system.vo.DictModel;
import cn.qingweico.common.system.vo.LoginUser;

import java.sql.SQLException;
import java.util.List;


/**
 * @author zhouqingwei
 */
public interface ISysBaseAPI {

	/**
	 * 日志添加
	 * @param logContent 内容
	 * @param logType 日志类型(0:操作日志;1:登录日志;)
	 * @param operateType 操作类型(1:添加;2:修改;3:删除;)
	 */
	void addLog(String logContent, Integer logType, Integer operateType);

	/**
	  * 根据用户账号查询登录用户信息
	 * @param username
	 * @return
	 */
    LoginUser getUserByName(String username);

	/**
	 * 通过用户账号查询角色集合
	 * @param username
	 * @return
	 */
    List<String> getRolesByUsername(String username);

	/**
	 * 获取当前数据库类型
	 * @return
	 * @throws Exception
	 */
    String getDatabaseType() throws SQLException;

	/**
	  * 获取数据字典
	 * @param code
	 * @return
	 */
    List<DictModel> queryDictItemsByCode(String code);

	/** 查询所有的父级字典, 按照created排序 */
    List<DictModel> queryAllDict();

	/**
	  * 获取表数据字典
	 * @param table
	 * @param text
	 * @param code
	 * @return
	 */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
   	 * 查询所有部门 作为字典信息 id -->value,departName -->text
   	 * @return
   	 */
    List<DictModel> queryAllDepartBackDictModel();
}
