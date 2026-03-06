package cn.qingweico.common.api.vo;


import com.alibaba.fastjson.JSON;
import lombok.Getter;

/**
 * 统一API响应结果封装
 *
 * @author zhouqingwei
 */
@Getter
public class ResultVo<T> {
    private int code;
    private String msg;
    private T data;
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 400;

    public ResultVo<T> setCode(final int resultCode) {
        this.code = resultCode;
        return this;
    }

    public ResultVo<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResultVo<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static ResultVo<String> genSuccessResult() {
        return new ResultVo<String>()
                .setCode(SUCCESS_CODE)
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> ResultVo<T> genSuccessResult(T data) {
        return new ResultVo<T>()
                .setCode(SUCCESS_CODE)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static <T> ResultVo<T> genFailResult(String msg, int code) {
        return new ResultVo<T>()
                .setCode(code)
                .setMsg(msg);
    }

    public static <T> ResultVo<T> genFailResult(String msg) {
        return new ResultVo<T>()
                .setCode(FAIL_CODE)
                .setMsg(msg);
    }

}
