package cn.qingweico.common.util.excel.html.css.impl;

import cn.qingweico.common.util.excel.html.css.ICssConvertToExcel;
import cn.qingweico.common.util.excel.html.css.ICssConvertToHtml;
import cn.qingweico.common.util.excel.html.entity.style.CellStyleEntity;
import cn.qingweico.common.util.excel.util.PoiCssUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class BackgroundCssConvertImpl implements ICssConvertToExcel, ICssConvertToHtml {

    @Override
    public String convertToHtml(Cell cell, CellStyle cellStyle, CellStyleEntity style) {

        return null;
    }

    @Override
    public void convertToExcel(Cell cell, CellStyle cellStyle, CellStyleEntity style) {
        if (StringUtils.isEmpty(style.getBackground())) {
            return;
        }
        // 填充图案
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (cell instanceof XSSFCell) {
            ((XSSFCellStyle) cellStyle)
                    .setFillForegroundColor(PoiCssUtils.parseColor(style.getBackground()));
        } else if (cell instanceof HSSFCell) {
            cellStyle.setFillForegroundColor(
                    PoiCssUtils.parseColor((HSSFWorkbook) cell.getRow().getSheet().getWorkbook(),
                            style.getBackground()).getIndex());
        }
    }

}
