package cn.qingweico.common.util;

/**
 * 响应码枚举, 参考HTTP状态码的语义
 */
public final class ResultCode {
	/**
	 * 成功
	 */
	public static final int SUCCESS = 200;
	/**
	 * 失败
	 */
	public static final int FAIL = 400;
	/**
	 * 未认证(签名错误)未授权
	 */
	public static final int UNAUTHORIZED = 401;
	/**
	 * token失效过期
	 */
	public static final int EXPIRED_TOKEN = 402;
	/**
	 * 禁止访问
	 */
	public static final int FORBIDDEN = 403;
	/**
	 * 接口不存在
	 */
	public static final int NOT_FOUND = 404;
	/**
	 * 重复登录
	 */
	public static final int DUPLICATE_LOGIN = 405;
	/**
	 * 无效客户端
	 */
	public static final int INVALID_CLIENT = 406;
	/**
	 * 参数错误
	 */
	public static final int PARAMETER_ERROR = 407;
	/**
	 * 请求超时
	 */
	public static final int REQUEST_TIMEOUT = 408;
	/**
	 * 签名错误
	 */
	public static final int SIGN_ERROR = 409;
	/**
	 * api接口不存在
	 */
	public static final int API_NOT_FOUND = 410;
	/**
	 * 版本错误
	 */
	public static final int VERSION_ERROR = 411;
	/**
	 * 序列化异常
	 */
	public static final int SERIAL_ERROR = 412;
	/**
	 * token无效
	 */
	public static final int TOKEN_INVALID = 413;
	/**
	 * token为空
	 */
	public static final int TOKEN_NULL = 414;
	/**
	 * 用户账号锁定
	 */
	public static final int USER_ACCOUNT_LOCK = 415;
	/**
	 * 用户不存在
	 */
	public static final int USER_NOT_EXIST = 416;
	/**
	 * 非法用户
	 */
	public static final int USER_UNLAWFUL = 417;
	/**
	 * 服务器内部错误
	 */
	public static final int INTERNAL_SERVER_ERROR = 500;
	/**
	 * 车辆无入场纪录
	 */
	public static final int NO_ENTER_LOG = 1000;

}
