package cn.qingweico.entity;

import cn.qingweico.anno.AutoFill;
import cn.qingweico.anno.FieldType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zqw
 * @date 2025/7/18
 */
@Data
public class SysDept implements BaseEntity {
    private String deptNo;
    private String deptName;
    private String state;
    private String remark;
    private String parentDeptNo;
    @AutoFill(FieldType.CREATE_TIME)
    private LocalDateTime createTime;
    @AutoFill(FieldType.UPDATE_TIME)
    private LocalDateTime updateTime;
}
