package cn.qingweico.common.util.excel.entity.result;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * 导入返回类
 *
 * @author JueYue
 *  2014年6月29日 下午5:12:10
 */
public class ExcelImportResult<T> {

    /**
     * 结果集
     */
    private List<T> list;
    /**
     * 失败数据
     */
    private List<T>  failList;

    /**
     * 是否存在校验失败
     */
    private boolean  verfiyFail;

    /**
     * 数据源
     */
    private Workbook workbook;
    /**
     * 失败的数据源
     */
    private Workbook failWorkbook;

    private Map<String,Object> map;

    public ExcelImportResult() {

    }

    public ExcelImportResult(List<T> list, boolean verfiyFail, Workbook workbook) {
        this.list = list;
        this.verfiyFail = verfiyFail;
        this.workbook = workbook;
    }

    public List<T> getList() {
        return list;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public boolean isVerfiyFail() {
        return verfiyFail;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setVerfiyFail(boolean verfiyFail) {
        this.verfiyFail = verfiyFail;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Workbook getFailWorkbook() {
        return failWorkbook;
    }

    public void setFailWorkbook(Workbook failWorkbook) {
        this.failWorkbook = failWorkbook;
    }

    public List<T> getFailList() {
        return failList;
    }

    public void setFailList(List<T> failList) {
        this.failList = failList;
    }
}
