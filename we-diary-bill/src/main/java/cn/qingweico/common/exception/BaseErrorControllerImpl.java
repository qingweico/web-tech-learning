package cn.qingweico.common.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Maps;
import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.system.util.R;
import cn.qingweico.common.util.ResultCode;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @author zhouqingwei
 */
@Controller
public class BaseErrorControllerImpl extends BasicErrorController {


    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        ErrorProperties errorProperties = this.getErrorProperties();
        errorProperties.setIncludeException(true);
        errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);

        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        Object attribute = request.getAttribute("javax.servlet.error.exception");

        if (null != attribute) {
            body = Maps.newHashMap();
            String jsonStr = JSONObject.parseObject(JSON.toJSONString(attribute), JSONObject.class, Feature.DisableSpecialKeyDetect).getJSONObject("cause").getString("message");
            body.putAll(JSON.parseObject(JSON.toJSONString(Result.error(JSONObject.parseObject(jsonStr).getInteger("code"), JSONObject.parseObject(jsonStr).getString("message")))));
            return new ResponseEntity<>(body, HttpStatus.OK);
        }

        body.putAll(JSON.parseObject(JSON.toJSONString(R.error(ResultCode.INTERNAL_SERVER_ERROR, String.valueOf(body.getOrDefault("message", ""))))));

        return new ResponseEntity<>(body, getStatus(request));
    }


    /**
     * 覆盖默认的HTML响应
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request,
                getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
    }


    public BaseErrorControllerImpl() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }


    public BaseErrorControllerImpl(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }


}
