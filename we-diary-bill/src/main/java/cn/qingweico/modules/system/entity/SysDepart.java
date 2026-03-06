package cn.qingweico.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.qingweico.common.aspect.annotation.Dict;
import cn.qingweico.common.util.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * @author zhouqingwei
 */
@Data
@TableName("sys_depart")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDepart implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**父机构ID*/
	private String parentId;
	/**机构/部门名称*/
	@Excel(name="机构/部门名称",width=15)
	private String departName;
	/**英文名*/
	@Excel(name="英文名",width=15)
	private String departNameEn;
	/**缩写*/
	private String departNameAbbr;
	/**排序*/
	@Excel(name="排序",width=15)
	private Integer departOrder;
	/**描述*/
	@Excel(name="描述",width=15)
	private Object description;
	/**机构类型*/
	@Excel(name="机构类型",width=15)
	private String orgType;
	/**机构编码*/
	@Excel(name="机构编码",width=15)
	private String orgCode;
	/**手机号*/
	@Excel(name="手机号",width=15)
	private String mobile;
	/**传真*/
	@Excel(name="传真",width=15)
	private String fax;
	/**地址*/
	@Excel(name="地址",width=15)
	private String address;
	/**备注*/
	@Excel(name="备注",width=15)
	private String memo;
	/**状态(1启用, 0不启用)*/
	@Excel(name="状态",width=15)
	@Dict(dicCode = "depart_status")
	private String status;
	/**删除状态(0, 正常, 1已删除)*/
	@Excel(name="删除状态",width=15)
	@Dict(dicCode = "del_flag")
	private String delFlag;
	/**创建人*/
	private String createdBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date created;
	/**更新人*/
	private String lastUpdBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpd;
}
