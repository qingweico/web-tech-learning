package cn.qingweico.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/11/10
 * @see org.apache.poi.hssf.usermodel.HSSFWorkbook (Horrible SpreadSheet Format)
 * 用于处理 .xls 格式的 Excel 文件, 基于二进制格式, 其兼容老版本, 内存占用较低, 无法使用
 * Excel 2007 及以上版本的新特
 * @see org.apache.poi.xssf.usermodel.XSSFWorkbook (XML SpreadSheet Format)
 * 用于处理 .xlsx 格式的 Excel 文件, 基于 OOXML(Office Open XML)标准, 本质上是一个 ZIP 压缩包
 * 其内存占用非常高, 易OOM, 支持 Excel 的所有新特性
 * @see org.apache.poi.xssf.streaming.SXSSFWorkbook (Streaming XML SpreadSheet Format)
 * 用于写入 .xlsx 格式的 Excel 文件, 是 {@link XSSFWorkbook} 的流式扩展
 * 其用来处理海量数据, 内存占用极低且可控, 写入速度非常快, 但是读取功能和部分高级功能受限
 */
@Service
public class ExcelService {
    @Resource
    private PdfGenerationService pdfGenerationService;


    public byte[] create() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("每日诗词");

        Map<String, Object> buildSend = pdfGenerationService.buildSend();
        String[] headers = buildSend.keySet().toArray(new String[0]);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        Collection<Object> values = buildSend.values();
        Row row = sheet.createRow(1);
        int col = 0;
        for (Object value : values) {
            Cell cell = row.createCell(col++);
            cell.setCellValue(value.toString());
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            workbook.write(os);
            workbook.close();
            return os.toByteArray();
        }
    }
}
