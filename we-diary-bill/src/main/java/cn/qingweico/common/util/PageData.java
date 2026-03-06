package cn.qingweico.common.util;

import org.apache.ibatis.type.Alias;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author Administrator
 */
@Alias("pd")
public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 1L;

    Map map = null;
    HttpServletRequest request;

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        map = returnMap;
    }

    public PageData() {
        map = new HashMap();
    }

    public static PageData newPageData() {
       return new PageData();
    }

    public PageData putAndReturn(Object key, Object value) {
        map.put(key, value);
        return this;
    }

    @Override
    public Object get(Object key) {
        Object obj = null;
        if (map.get(key) instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = map.get(key);
        }
        return obj;
    }


    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    public String getString(Object key) {
        Object objData = get(key);
        if (objData instanceof Date) {
            return DateUtils.dateToString((Date) objData, "yyyy-MM-dd");
        } else if (objData != null) {
            return objData.toString();
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }


    public Object removeAllKey(Object... keys) {
        return map.keySet().removeAll(Arrays.asList(keys));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set keySet() {
        return map.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map t) {
        map.putAll(t);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Collection values() {
        return map.values();
    }

}
