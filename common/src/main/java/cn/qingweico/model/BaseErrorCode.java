package cn.qingweico.model;

/**
 * @author zqw
 * @date 2025/8/1
 */

public enum BaseErrorCode implements IErrorCode {
    VALIDATE_FAILED("102", "字段校验失败"),
    SYSTEM_ERROR("510", "系统错误");

    private final String code;
    private final String msg;

    /**
     * 枚举构造方法
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    BaseErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
