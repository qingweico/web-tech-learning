package cn.qingweico.config;

import cn.hutool.core.util.StrUtil;
import cn.qingweico.convert.Convert;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zqw
 * @date 2025/8/2
 */
@Configuration
public class HttpCorsFilter extends OncePerRequestFilter implements Ordered {
    public HttpCorsFilter(CorsConfig config) {
        this.config = config;
    }

    private final CorsConfig config;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, StringUtils.join(config.getAllowedOrigins(), StrUtil.COMMA));
        if (config.getAllowedMethods() != null && config.getAllowedMethods().length > 0) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, StringUtils.join(config.getAllowedMethods(), StrUtil.COMMA));
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.toString(config.isAllowCredentials()));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, Convert.toString(config.getMaxAge()));

        if (config.getAllowedHeaders() != null && config.getAllowedHeaders().length > 0) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.join(config.getAllowedHeaders(), StrUtil.COMMA));
        }

        if (config.getExposeHeaders() != null && config.getExposeHeaders().length > 0) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, StringUtils.join(config.getExposeHeaders(), StrUtil.COMMA));
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10000;
    }
}
