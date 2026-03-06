package cn.qingweico.common.system.base.dao;

import java.util.List;

public interface DAO {

    /**
     * 保存对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object save(String str, Object obj) throws Exception;

    /**
     * 修改对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object update(String str, Object obj) throws Exception;

    /**
     * 修改对象
     *
     * @param str
     * @param objs
     * @return
     * @throws Exception
     */
    public void batchUpdate(String str, List objs) throws Exception;

    /**
     * 删除对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object delete(String str, Object obj) throws Exception;

    /**
     * 查找对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object findForObject(String str, Object obj) throws Exception;

    /**
     * 查找对象
     *
     * @param str
     * @param obj
     * @return
     * @throws Exception
     */
    public Object findForList(String str, Object obj) throws Exception;

    /**
     * 查找对象封装成Map
     *
     * @param sql
     * @param obj
     * @return
     * @throws Exception
     */
    public Object findForMap(String sql, Object obj, String key, String value) throws Exception;


    /**
     * ********************************************************************
     */
    public String compeatConfirm(String str, String param) throws Exception;

    public Object likeSearch(String str, Object obj);

    public Object chargeCompeatConfirm(String str, Object obj) throws Exception;

    /**
     * 获取字典
     */
    public Object getDictinory(String str, String DIC_TYPE) throws Exception;

    /**
     * 用户详情查询
     */
    public Object userDetailList(String str, Object obj) throws Exception;
}
