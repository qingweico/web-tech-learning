package cn.qingweico.common.util.excel.entity;

import java.util.List;

/**
 * excel 导入工具类,对cell类型做映射
 * @author JueYue
 * @version 1.0 2013年8月24日
 */
public class ExcelImportEntity extends ExcelBaseEntity {

    public final static String IMG_SAVE_PATH = "/excel/upload/img";
    /**
     * 对应 Collection NAME
     */
    private String collectionName;
    /**
     * 保存图片的地址
     */
    private String saveUrl;
    /**
     * 保存图片的类型,1是文件,2是数据库
     */
    private int saveType;
    /**
     * 对应exportType
     */
    private String classType;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * 导入校验字段
     */
    private boolean importField;

    /**
     * 枚举导入静态方法
     */
    private String enumImportMethod;

    private List<ExcelImportEntity> list;

    public String getClassType() {
        return classType;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public List<ExcelImportEntity> getList() {
        return list;
    }

    public int getSaveType() {
        return saveType;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setList(List<ExcelImportEntity> list) {
        this.list = list;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isImportField() {
        return importField;
    }

    public void setImportField(boolean importField) {
        this.importField = importField;
    }

    public String getEnumImportMethod() {
        return enumImportMethod;
    }

    public void setEnumImportMethod(String enumImportMethod) {
        this.enumImportMethod = enumImportMethod;
    }
}
