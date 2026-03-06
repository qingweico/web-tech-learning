package cn.qingweico.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import cn.qingweico.common.aspect.annotation.Dict;
import cn.qingweico.common.util.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * @author zhouqingwei
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 字典id
     */
    private String dictId;

    /**
     * 字典项文本
     */
    @Excel(name = "字典项文本", width = 20)
    private String itemText;

    /**
     * 字典项值
     */
    @Excel(name = "字典项值", width = 30)
    private String itemValue;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 40)
    private String description;

    /**
     * 排序
     */
    @Excel(name = "排序", width = 15,type=4)
    private Integer sortOrder;


    /**
     * 状态(1启用 0不启用)
     */
    @Dict(dicCode = "dict_item_status")
    private Integer status;

    private String createdBy;

    private Date created;

    private String lastUpdBy;

    private Date lastUpd;


}
