package cn.qingweico.common.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author jdd
 */
@Slf4j
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private Map<String , String[]> params = new HashMap<String, String[]>();
    private  byte[] data = new byte[]{};

    private static final String CHASET_ENCODING = "UTF-8";

    HttpServletRequest orgRequest;

    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
        this.params.putAll(request.getParameterMap());
        try {
            data = StreamUtils.copyToByteArray(request.getInputStream());
            Map requetJsonMap = JSON.parseObject(new String(data, CHASET_ENCODING));
            if (requetJsonMap != null) {
                this.params.putAll(requetJsonMap);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }





    public ParameterRequestWrapper(HttpServletRequest request, String content) {
        super(request);
        data = content.getBytes(Charset.forName("UTF-8"));
    }

    public ParameterRequestWrapper(HttpServletRequest request, Map params) {
        super(request);
        this.params = params;
    }

    public JSON getRequestBody() throws UnsupportedEncodingException {
        return JSON.parseObject((new String(data, CHASET_ENCODING)));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return !isReady();
            }

            @Override
            public boolean isReady() {
                return inputStream.available() > 0;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }


    public void setInputStream(String content) throws UnsupportedEncodingException {
        this.data = content.getBytes(CHASET_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return super.getReader();
    }

    @Override
    public String getParameter(String name) {
        Object v = params.get(name);
        if (v == null) {
            return null;
        } else if (v instanceof String[]) {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                return strArr[0];
            } else {
                return null;
            }
        } else if (v instanceof String) {
            return (String) v;
        } else {
            return v.toString();
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return params;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Vector names = new Vector(params.keySet());
        return names.elements();
    }

    @Override
    public String[] getParameterValues(String name) {
        Object v = params.get(name);
        if (v == null) {
            return null;
        } else if (v instanceof String[]) {
            return (String[]) v;
        } else if (v instanceof String) {
            return new String[] { (String) v };
        } else {
            return new String[] { v.toString() };
        }
    }

    public void addAllParameters(Map<String , Object>otherParams) {//增加多个参数
        for(Map.Entry<String , Object>entry : otherParams.entrySet()) {
            addParameter(entry.getKey() , entry.getValue());
        }
    }


    public void addParameter(String name , Object value) {//增加参数
        if(value != null) {
            if(value instanceof String[]) {
                params.put(name , (String[])value);
            }else if(value instanceof String) {
                params.put(name , new String[] {(String)value});
            }else {
                params.put(name , new String[] {String.valueOf(value)});
            }
        }
    }

}
