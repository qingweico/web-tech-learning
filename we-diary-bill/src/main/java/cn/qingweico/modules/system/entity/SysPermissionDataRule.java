package cn.qingweico.modules.system.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author zhouqingwei
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPermissionDataRule implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;

	/**
	 * 对应的菜单id
	 */
	private String permissionId;

	/**
	 * 规则名称
	 */
	private String ruleName;

	/**
	 * 字段
	 */
	private String ruleColumn;

	/**
	 * 条件
	 */
	private String ruleConditions;

	/**
	 * 规则值
	 */
	private String ruleValue;

	/**
	 * 状态值 1有效 0无效
	 */
	private String status;

	/**
	 * 创建时间
	 */
	private Date created;

	/**
	 * 创建人
	 */
	private String createdBy;

	/**
	 * 修改时间
	 */
	private Date lastUpd;

	/**
	 * 修改人
	 */
	private String lastUpdBy;
}
