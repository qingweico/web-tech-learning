package cn.qingweico.common.util.excel.entity.params;

import cn.qingweico.common.util.excel.handler.IExcelDataHandler;
import cn.qingweico.common.util.excel.handler.IExcelDictHandler;
import cn.qingweico.common.util.excel.handler.IExcelI18nHandler;

/**
 * 基础参数
 * @author JueYue
 *  2014年6月20日 下午1:56:52
 */
@SuppressWarnings("rawtypes")
public class ExcelBaseParams {

    /**
     * 数据处理接口,以此为主,replace,format都在这后面
     */
    private IExcelDataHandler dataHandler;

    /**
     * 字段处理类
     */
    private IExcelDictHandler dictHandler;
    /**
     * 国际化处理类
     */
    private IExcelI18nHandler i18nHandler;

    public IExcelDataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(IExcelDataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public IExcelDictHandler getDictHandler() {
        return dictHandler;
    }

    public void setDictHandler(IExcelDictHandler dictHandler) {
        this.dictHandler = dictHandler;
    }

    public IExcelI18nHandler getI18nHandler() {
        return i18nHandler;
    }

    public void setI18nHandler(IExcelI18nHandler i18nHandler) {
        this.i18nHandler = i18nHandler;
    }
}
