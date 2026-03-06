package cn.qingweico.common.util.excel;


import cn.qingweico.common.util.ReflectUtils;
import cn.qingweico.common.util.excel.entity.ExcelData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ExcelUtil {


    private Workbook workbook;
    private OutputStream os;
    /**
     * 日期格式
     */
    private String pattern;
    /**
     * 2003- 版本的excel
     */
    private final static String excel2003L = ".xls";
    /**
     * 2007+ 版本的excel
     */
    private final static String excel2007U = ".xlsx";
    /**
     * poi特殊日期格式: 数字格式化成-yyyy年MM月dd日, 格式
     */
    private static ArrayList<String> PoiDateList = new ArrayList<String>() {
        {
            add("年");
            add("月");
            add("日");
        }
    };

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public ExcelUtil(Workbook workboook) {
        this.workbook = workboook;
    }

    /**
     * 描述: 根据文件后缀, 自适应上传文件的版本
     *
     * @param inStr    将file.getInputStream()获取的输入流
     * @param fileName file.getOriginalFilename()获取的原文件名
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            /**
             * 2003
             */
            wb = new HSSFWorkbook(inStr);
        } else if (excel2007U.equals(fileType)) {
            /**
             * 2007
             */
            wb = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 获取某sheet某行某列的数据
     *
     * @param sheetIx
     * @param rowNum
     * @param colNum
     * @return
     * @throws Exception
     */
    public String readCell(int sheetIx, int rowNum, int colNum) throws Exception {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            return null;
        }
        return cell.getStringCellValue();
    }


    public static void exportExcel(HttpServletResponse response, String fileName, ExcelData data) throws Exception {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportExcel(data, response.getOutputStream());
    }

    public static void exportExcel(ExcelData data, OutputStream out) throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = data.getName();

            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, data, 65535);

            wb.write(out);
        } finally {
            wb.close();
        }
    }

    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelData data, int sheetSize) {

        int rowIndex = 0;

        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles());

        if (data.getRows().size() == 0 || data.getRows() == null) {
            //throw new ExcelException("数据源中没有任何数据");
        }

        if (sheetSize > 65535 || sheetSize < 1) {
            sheetSize = 65535;
        }


        //因为2003的Excel一个工作表最多可以有65536条记录, 除去列头剩下65535条
        //所以如果记录太多, 需要放到多个工作表中, 其实就是个分页的过程
        //1.计算一共有多少个工作表
        int totalSize = data.getRows().size();
        int sheetNum = totalSize / sheetSize;
        for (int i = 0; i <= sheetNum; i++) {
            int start = sheetSize * sheetNum;
            int end = sheetSize * (sheetNum + 1) > totalSize ? totalSize : sheetSize * (sheetNum + 1);
            writeRowsToExcel(wb, sheet, data.getRows().subList(start, end), data.getSqlColumns(), rowIndex);
            autoSizeColumns(sheet, data.getTitles().size() + 1);
        }

    }

    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex = 0;

        Font titleFont = wb.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        // titleFont.setFontHeightInPoints((short) 14);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(182, 184, 192), new DefaultIndexedColorMap()));
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new java.awt.Color(0, 0, 0), new DefaultIndexedColorMap()));

        Row titleRow = sheet.createRow(rowIndex);
        // titleRow.setHeightInPoints(25);
        colIndex = 0;

        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    private static <T> int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<T> rows, List<String> sqlColumns, int rowIndex) {
        int colIndex = 0;

        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setFont(dataFont);
        dataStyle.setWrapText(true);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new java.awt.Color(0, 0, 0), new DefaultIndexedColorMap()));

        for (T rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            // dataRow.setHeightInPoints(25);
            colIndex = 0;
            Object cellData;
            for (String sqlColumn : sqlColumns) {
                Cell cell = dataRow.createCell(colIndex);
                if (rowData instanceof Map) {
                    Map<String, Object> map = (Map) rowData;
                    cellData = map.get(sqlColumn);
                } else {
                    cellData = ReflectUtils.getFieldValue(rowData, sqlColumn);
                }
                if (cellData != null) {
                    cell.setCellValue(cellData.toString().replaceAll("<br/>", "\r\n"));
                } else {
                    cell.setCellValue("");
                }

                cell.setCellStyle(dataStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    private static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }


    protected String convertVal(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    cellValue = cell.getRichStringCellValue().getString().trim();
                    break;
                case NUMERIC:
                    String dataFormat = cell.getCellStyle().getDataFormatString();
                    //  判断格式化信息中是否存在: 年月日
                    AtomicReference<Boolean> isDate = new AtomicReference<>(false);
                    if (!StringUtils.isEmpty(dataFormat)) {
                        PoiDateList.forEach(x -> isDate.set(isDate.get() || dataFormat.contains(x)));
                    }

                    if (DateUtil.isCellDateFormatted(cell) || DateUtil.isCellInternalDateFormatted(cell)) {
                        cellValue = cn.qingweico.common.util.DateUtil.fomatDateTimeStr(DateUtil.getJavaDate(cell.getNumericCellValue()));
                    }
                    //有些情况, 时间搓？数字格式化显示为时间,不属于上面两种时间格式
                    else if (isDate.get()) {
                        cellValue =cn.qingweico.common.util.DateUtil.fomatDateStr(cell.getDateCellValue());
                    }
                    else {
                        double contentNumber = cell.getNumericCellValue();
                        if ((contentNumber - (int) contentNumber) < 4.9E-324D) {
                            cellValue = Integer.toString((int) contentNumber);
                        } else {
                            DecimalFormat dfs = new DecimalFormat("0");
                            cellValue = dfs.format(cell.getNumericCellValue());
                        }
                    }
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
                    break;
                case FORMULA:
                    cellValue = cell.getCellFormula();
                    break;
                case ERROR:
                    cellValue = FormulaError.forInt(cell.getErrorCellValue()).getString();
                    break;
                case BLANK:
                default:
                    cellValue = "";
            }
        }
        return cellValue;
    }

    protected String replaceContent(String content) {
        if ("".equals(content)) {
            return "";
        }
        content = content.trim();
        content = content.replace("–", "-");
        content = content.replace("、", ",");
        content = content.replace(", ", ",");
        content = content.replace(", ,", ",");
        content = content.replace("\r\n", "");
        content = content.replace("\n", "");
        content = content.replace(';', ',');
        content = content.replace("?", "");
        content = content.replace("  ", "");
        content = content.replace("？", "");
        content = content.replace(" ", "");
        content = content.endsWith(",") ? content.substring(0, content.length() - 1) : content;

        return content;
    }

}
