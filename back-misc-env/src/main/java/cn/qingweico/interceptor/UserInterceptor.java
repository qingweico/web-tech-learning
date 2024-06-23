package cn.qingweico.interceptor;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zqw
 * @date 2024/6/22
 */
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 获取到用户标识
        String userNo = getUserNo(request);
        // 把用户 ID 放到 MDC 上下文中
        MDC.put("userId", userNo);
        return true;
    }

    private String getUserNo(HttpServletRequest request) {
        // 通过 SSO 或者Cookie 或者 Auth信息获取到 当前登陆的用户信息
        return request.getRemoteAddr();
    }
}
