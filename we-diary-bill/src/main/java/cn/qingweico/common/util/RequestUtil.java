package cn.qingweico.common.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class RequestUtil {

    public static String getValue() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pd = new PageData(request);

        Map properties = request.getParameterMap();
        Map returnMap = new HashMap(16);
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";

        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
        }

        return name;
    }

    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        String url = request.getServletPath();
        if (request.getPathInfo() != null) {
            url = url + request.getPathInfo();
        }
        return url;
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }


    public static String getRequestURLStr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getRequestURLStr(request);
    }

    public static String getRequestURLStr(HttpServletRequest request) {
        return getRequestURLStr(request, Maps.newHashMap(), new String[]{});
    }



    /**
     * @param request
     * @return
     */
    public static String getRequestURLStr(HttpServletRequest request, Map<String, String> params, String... excludes) {
        Map<String, String> sortedMap = new TreeMap<String, String>();
        boolean hasParam = false;
        StringBuilder sb = new StringBuilder(getRequestUrl());
        sb.append("?");
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            if (StringUtils.isEmpty(request.getParameter(paraName))) {
                sortedMap.put(paraName, "");
            } else {
                sortedMap.put(paraName, request.getParameter(paraName));
            }
        }
        sortedMap.putAll(params);
        Set<Map.Entry<String, String>> entries = sortedMap.entrySet();
        Set<String> excludesSets = Sets.newHashSet(excludes);
        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (excludesSets.contains(name)) {
                continue;
            }
            // 忽略参数名或参数值为空的参数
            if (StringUtils.isNoneBlank(name, value.replace(' ', '+'))) {
                if (hasParam) {
                    sb.append("&");
                } else {
                    hasParam = true;
                }
                sb.append(name).append("=").append(value);
            }

        }
        return sb.toString();
    }

}
