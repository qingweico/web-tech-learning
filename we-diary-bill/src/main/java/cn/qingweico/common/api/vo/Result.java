package cn.qingweico.common.api.vo;

import cn.qingweico.common.constant.CommonConstant;
import cn.qingweico.common.constant.JddConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;


/**
 * @author zhouqingwei
 */
@Data
@Accessors(chain = true)
@ApiModel(value="接口返回对象", description="接口返回对象")
public class Result<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@ApiModelProperty(value = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message = "操作成功";

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回代码")
	private Integer code = 0;

	/**
	 * 返回数据对象 data
	 */
	@ApiModelProperty(value = "返回数据对象")
	private T result;

	/**
	 * 时间戳
	 */
	@ApiModelProperty(value = "时间戳")
	private long timestamp = System.currentTimeMillis();

	public Result() {

	}

	public Result<T> error500(String message) {
		this.message = message;
		this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
		this.success = false;
		return this;
	}

	public Result<T> success(String message) {
		this.message = message;
		this.code = CommonConstant.SC_OK_200;
		this.success = true;
		return this;
	}


	public static Result<Object> ok() {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage("成功");
		return r;
	}

	public static <M> Result<M> ok(String msg) {
		Result<M> r = new Result<>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage(msg);
		return r;
	}

	public static <M> Result<M> ok(M data) {
		Result<M> r = new Result<>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setResult(data);
		return r;
	}

	public static <M> Result<M> error(String msg) {
		return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
	}

	public static <M> Result<M> error(int code, String msg) {
		Result<M> r = new Result<>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

	public static String getValidErrorMsg(Collection<ObjectError> errList) {

		for (ObjectError obj : errList) {
			return obj.getDefaultMessage();
		}
		return "";
	}

	public static Result<?> validError(BindingResult result) {
		return Result.error(JddConstant.SC_JEECG_NO_PARAM,
				getValidErrorMsg(result.getAllErrors()));
	}
	/**
	 * 无权限访问返回结果
	 */
	public static Result<Object> noAuth(String msg) {
		return error(CommonConstant.SC_JEECG_NO_AUTHZ, msg);
	}
}
