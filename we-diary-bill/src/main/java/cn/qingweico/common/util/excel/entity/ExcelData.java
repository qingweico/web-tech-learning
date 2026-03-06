package cn.qingweico.common.util.excel.entity;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelData<T> implements Serializable {
    private static final long serialVersionUID = 4444017239100620999L;

    /**
     * 表头
     */
    private List<String> titles;

    private List<String> sqlColumns;
    /**
     * 数据
     */
    private List<T> rows;
    /**
     * 页签名称
     */
    private String name;

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<String> getSqlColumns() {
        return sqlColumns;
    }

    public void setSqlColumns(List<String> sqlColumns) {
        this.sqlColumns = sqlColumns;
    }

    public ExcelData(List<T> rows, List<String> titles, List<String> sqlColumns) {
        this.rows = rows;
        this.titles = titles;
        this.sqlColumns = sqlColumns;
    }

    public ExcelData(List<T> mapList) {
        titles = new ArrayList<String>();
        rows = new ArrayList<T>();
        sqlColumns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mapList)) {
            T tempT = mapList.get(0);
            if (tempT instanceof Map) {
                Map<String, Object> tempMap = (Map<String, Object>) tempT;
                for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
                    if ("GUUID".equals(entry.getKey())) {
                        continue;
                    }
                    titles.add(entry.getKey());
                    sqlColumns.add(entry.getKey());
                }
            } else {

            }
        }

        this.rows.addAll(mapList);
    }
}
