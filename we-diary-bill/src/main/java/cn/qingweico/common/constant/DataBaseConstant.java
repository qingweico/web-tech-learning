package cn.qingweico.common.constant;

/**
 * 数据库上下文常量
 * @author zhouqingwei
 */
public interface DataBaseConstant {


    /**
     * 数据-所属机构编码
     */
    String SYS_ORG_CODE = "sysOrgCode";
    /**
     * 数据-所属机构编码
     */
    String SYS_MULTI_ORG_CODE = "sysMultiOrgCode";

    /**
     * 数据-所属机构编码
     */
    String SYS_ORG_CODE_TABLE = "sys_org_code";
    /**
     * 数据-系统用户编码(对应登录用户账号)
     */
    String SYS_USER_CODE = "sysUserCode";
    /**
     * 数据-系统用户编码(对应登录用户账号)
     */
    String SYS_USER_CODE_TABLE = "sys_user_code";

    /**
     * 登录用户真实姓名
     */
    String SYS_USER_NAME = "sysUserName";
    /**
     * 登录用户真实姓名
     */
    String SYS_USER_NAME_TABLE = "sys_user_name";
    /**
     * 系统日期"yyyy-MM-dd"
     */
    String SYS_DATE = "sysDate";
    /**
     * 系统日期"yyyy-MM-dd"
     */
    String SYS_DATE_TABLE = "sys_date";
    /**
     * 系统时间"yyyy-MM-dd HH:mm"
     */
    String SYS_TIME = "sysTime";
    /**
     * 系统时间"yyyy-MM-dd HH:mm"
     */
    String SYS_TIME_TABLE = "sys_time";
    //*********系统上下文变量****************************************


    //*********系统建表标准字段****************************************
    /**
     * 创建者登录名称
     */
    String CREATE_BY_TABLE = "create_by";
    /**
     * 创建者登录名称
     */
    String CREATE_BY = "createdBy";
    /**
     * 创建日期时间
     */
    String CREATE_TIME_TABLE = "created";
    /**
     * 创建日期时间
     */
    String CREATE_TIME = "created";
    /**
     * 更新用户登录名称
     */
    String UPDATE_BY_TABLE = "update_by";
    /**
     * 更新用户登录名称
     */
    String UPDATE_BY = "lastUpdBy";
    /**
     * 更新日期时间
     */
    String UPDATE_TIME = "lastUpd";
    /**
     * 更新日期时间
     */
    String UPDATE_TIME_TABLE = "update_time";


}
