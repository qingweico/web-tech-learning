package cn.qingweico.common.util.excel.entity;

import cn.qingweico.common.util.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 常森
 * @date 2022/1/19 17:19
 */
@Data
@AllArgsConstructor
public class ExcelErrorInfoExport {

    /**
     * cuo wu xin xi
     */
    @Excel(name = "错误原因", width = 100)
    private String msg;
}
