package cn.qingweico.common.util.excel.csv;

import cn.qingweico.common.util.excel.csv.entity.CsvExportParams;
import cn.qingweico.common.util.excel.csv.export.CsvExportService;
import cn.qingweico.common.util.excel.entity.ExcelExportEntity;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * Csv批量导出文件
 *
 * @author by jueyue on 18-11-14.
 */
public final class CsvExportUtil {

    /**
     * @param params    表格标题属性
     * @param pojoClass Excel对象Class
     * @param dataSet   Excel对象数据List
     */
    public static void exportCsv(CsvExportParams params, Class<?> pojoClass,
                                 Collection<?> dataSet, OutputStream outputStream) {
        new CsvExportService().createCsv(outputStream, params, pojoClass, dataSet);
    }

    /**
     * 根据Map创建对应的Excel
     *
     * @param params     表格标题属性
     * @param entityList Map对象列表
     * @param dataSet    Excel对象数据List
     */
    public static void exportCsv(CsvExportParams params, List<ExcelExportEntity> entityList,
                                 Collection<?> dataSet, OutputStream outputStream) {
        new CsvExportService().createCsvOfList(outputStream, params, entityList, dataSet);
    }
}
