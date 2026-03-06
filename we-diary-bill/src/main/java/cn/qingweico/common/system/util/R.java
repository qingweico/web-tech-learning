package cn.qingweico.common.system.util;

import cn.qingweico.common.util.ResultCode;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouqingwei
 */
@Data
@Accessors(chain = true)
public class R<T> {
    public static final int NO_LOGIN = -1;
    public static final int SUCCESS = ResultCode.SUCCESS;
    public static final int FAIL = 500;
    public static final int NO_PERMISSION = 2;
    private int code = SUCCESS;
    private T data;
    private String msg = "success";
    private Long timestamp = System.currentTimeMillis();
    private boolean success = true;

    public R() {
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String msg) {
        this(data);
        this.msg = msg;
    }

    public R(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public R(int code, T data, String msg) {
        this(data, msg);
        this.code = code;
    }

    public R(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = FAIL;
    }

    public boolean isSuccess() {
        return this.code == SUCCESS;
    }

    public static R error() {
        return error(FAIL, "未知异常, 请联系管理员");
    }

    public static R error(String msg) {
        return error(FAIL, msg);
    }

    public static <M> R<M> error(M data, String msg) {
        return error(FAIL, data, msg);
    }

    public static <M> R<M> error(int code, M data, String msg) {
        return new R<>(code, data, msg);
    }

    public static <M> R<M> error(int code, String msg) {
        return new R<>(code, msg);
    }

    public static <M> R<M> ok(M data) {
        return new R<>(data);
    }

    public static <M> R<M> ok(M data, String msg) {
        return new R<>(SUCCESS, data, msg);
    }

    public static <M> R<M> ok(int code, M data) {
        return new R<>(code, data, "");
    }

}
