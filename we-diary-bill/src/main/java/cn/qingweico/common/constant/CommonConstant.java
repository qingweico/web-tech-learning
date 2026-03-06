package cn.qingweico.common.constant;

/**
 * @author zhouqingwei
 */
public interface CommonConstant {


    /**
     * 删除标志
     */
    Integer DEL_FLAG_1 = 1;

    /**
     * 未删除
     */
   Integer DEL_FLAG_0 = 0;


    /**
     * 系统日志类型: 登录
     */
   int LOG_TYPE_1 = 1;

    /**
     * 系统日志类型: 操作
     */
   int LOG_TYPE_2 = 2;


    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 510
     */
    Integer SC_JEECG_NO_AUTHZ = 510;

    /**
     * 登录用户拥有角色缓存KEY前缀
     */
    String LOGIN_USER_CACHE_RULES_ROLE = "loginUser_cacheRules::Roles_";
    /**
     * 登录用户拥有权限缓存KEY前缀
     */
    String LOGIN_USER_CACHE_RULES_PERMISSION = "loginUser_cacheRules::Permissions_";
    /**
     * 登录用户令牌缓存KEY前缀 3600秒即是一小时
     */
    int TOKEN_EXPIRE_TIME = 3600;

    String PREFIX_USER_TOKEN = "prefix:user_token:";
    /**
     * echache 缓存名称 token
     */
    String TOKEN_CACHE_NAME = "token";
    /**
     * echache 缓存名称 captcha
     */
    String CAPTCHA_CACHE_NAME = "captcha";
    /**
     * 0:一级菜单
     */
    Integer MENU_TYPE_0 = 0;
    /**
     * 1:子菜单
     */
    Integer MENU_TYPE_1 = 1;
    /**
     * 2:按钮权限
     */
    Integer MENU_TYPE_2 = 2;
    /**
     * 通告对象类型(USER:指定用户, ALL:全体用户)
     */
    String MSG_TYPE_USER = "USER";
    String MSG_TYPE_ALL = "ALL";

    String HAS_SEND = "1";
    /**
     * 阅读状态(0未读, 1已读)
     */
    String HAS_READ_FLAG = "1";
    String NO_READ_FLAG = "0";
    /**
     * 优先级(L低, M中, H高)
     */
    String PRIORITY_L = "L";
    String STATUS_1 = "1";
    Integer RULE_FLAG_0 = 0;
    Integer RULE_FLAG_1 = 1;
    Integer USER_FREEZE = 2;
    /**
     * 字典翻译文本后缀
     */
    String DICT_TEXT_SUFFIX = "_dictText";
    String DEFAULT_CHARSET = "UTF-8";
    /**
     * 无手持端权限
     */
    Integer SIGN_MOBILE_NONE = 0;
}
