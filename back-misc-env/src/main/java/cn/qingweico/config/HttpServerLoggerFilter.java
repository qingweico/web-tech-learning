package cn.qingweico.config;

import cn.hutool.core.text.AntPathMatcher;
import cn.qingweico.convert.StringConvert;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @author zqw
 * @date 2025/8/28
 */
@Component
public class HttpServerLoggerFilter extends OncePerRequestFilter implements Ordered {
    private final Logger logger = LoggerFactory.getLogger(HttpServerLoggerFilter.class);

    Charset defaultCharset = StandardCharsets.UTF_8;
    @SuppressWarnings("FieldCanBeLocal")
    private final String mdcKey = "request-url";
    @SuppressWarnings("FieldCanBeLocal")
    private final int maxLength = 8 * 1024;
    private MediaType[] mediaTypes;
    @SuppressWarnings("FieldCanBeLocal")
    private final boolean urlDecode = false;
    @SuppressWarnings("FieldCanBeLocal")
    private final long longExecute = 800;
    private boolean head;

    public HttpServerLoggerFilter(HttpLoggerProperties loggerProperties) {
        if (loggerProperties.getMediaTypes() != null) {
            this.mediaTypes = loggerProperties.getMediaTypes();
            this.head = loggerProperties.isHead();
        }
    }
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filter) throws ServletException, IOException {


        boolean match = false;
        String url = null;

        ContentCachingRequestWrapper requestWrapper = null;
        try {

            AntPathMatcher antPathMatcher = new AntPathMatcher();
            url = request.getRequestURI();

            if (!antPathMatcher.match("/**/actuator/**", url) && !url.endsWith(".js") && !url.endsWith(".gif") && !url.endsWith(".jpg") && !url.endsWith(".bmp")
                    && !url.endsWith(".jpeg") && !url.endsWith(".png") && !url.endsWith(".css") && !url.endsWith(".ico") && !url.endsWith(".html") && !url.endsWith(".woff") && !url.endsWith(".woff2")) {

                match = true;

                MDC.put(mdcKey, request.getRequestURL().toString());

                if (logger.isInfoEnabled()) {
                    logger.info("{} => {}{}", request.getMethod(), request.getRequestURL().toString(), StringUtil.isEmpty(request.getQueryString()) ? "" : ("?" + request.getQueryString()));

                    if (logger.isDebugEnabled()) {
                        StringBuilder sb = new StringBuilder();
                        if (head) {
                            sb.append("\n------------Header------------\n");
                            Enumeration<String> headerKey = request.getHeaderNames();
                            while (headerKey.hasMoreElements()) {
                                String k = headerKey.nextElement();
                                String v = request.getHeader(k);
                                sb.append(k).append("=").append(v).append("\n");
                            }
                        }
                        String contentType = request.getContentType();
                        if(StringUtils.isNotEmpty(contentType)) {
                            final MediaType media = MediaType.parseMediaType(contentType);
                            if (Arrays.stream(mediaTypes).anyMatch(p -> p.isCompatibleWith(media))) {
                                sb.append("\n------------Body------------\n");
                                requestWrapper = new ContentCachingRequestWrapper(request);
                                byte[] data = requestWrapper.getContentAsByteArray();
                                if (data.length > 0) {
                                    Charset charset = media.getCharset() == null ? defaultCharset : media.getCharset();
                                    String characterEncoding = StringConvert.toString(request.getCharacterEncoding(), "UTF-8");
                                    if (urlDecode && MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(media)) {

                                        if (maxLength > data.length) {
                                            sb.append(URLDecoder.decode(new String(data, charset)
                                                    , characterEncoding));
                                        } else {
                                            sb.append(URLDecoder.decode(new String(data, 0, maxLength, charset), characterEncoding));
                                            sb.append(" ...(已省略)");
                                        }
                                    } else {
                                        if (maxLength > data.length) {
                                            sb.append(new String(data, charset));
                                        } else {
                                            sb.append(new String(data, 0, maxLength, charset));
                                            sb.append(" ...(已省略)");
                                        }
                                    }
                                } else {
                                    sb.append("BODY为空");
                                }
                                sb.append("\n");
                            }
                        }
                        sb.append("------------End------------\n");
                        logger.debug(sb.toString());
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("请求日志输出失败", e);
        }

        long startTime = System.currentTimeMillis();


        ContentCachingResponseWrapper responseWrapper = null;

        if (match && logger.isDebugEnabled()) {
            responseWrapper = new ContentCachingResponseWrapper(response);
        }

        try {
            filter.doFilter(requestWrapper != null ? requestWrapper : request, responseWrapper != null ? responseWrapper : response);
        } finally {
            if (match) {
                long endTime = System.currentTimeMillis();

                if (endTime - startTime >= longExecute) {
                    logger.warn("HTTP服务执行时间过长,花费了[{}]毫秒,URL => {}", endTime - startTime, url);
                }

                if (responseWrapper != null && responseWrapper.getContentType() != null) {
                    final MediaType media = MediaType.parseMediaType(responseWrapper.getContentType());
                    if (Arrays.stream(mediaTypes).anyMatch(p -> p.isCompatibleWith(media))) {
                        int size = responseWrapper.getContentSize();
                        Charset charset = media.getCharset() == null ? defaultCharset : media.getCharset();
                        if (size > maxLength) {
                            logger.debug("服务器HTTP响应 [{}] {}ms => {} ...(已省略)", responseWrapper.getStatus(), endTime - startTime, new String(responseWrapper.getContentAsByteArray(), 0, maxLength, charset));
                        } else {
                            logger.debug("服务器HTTP响应 [{}] {}ms => {}", responseWrapper.getStatus(), endTime - startTime, new String(responseWrapper.getContentAsByteArray(), charset));
                        }
                    } else {
                        logger.debug("服务器HTTP响应 [{}] {}ms => 报文体类型[{}]不支持日志显示", responseWrapper.getStatus(), endTime - startTime, media);
                    }
                    responseWrapper.copyBodyToResponse();
                } else {
                    logger.debug("服务器HTTP响应 [{}] {}ms", response.getStatus(), endTime - startTime);
                }
                MDC.remove(mdcKey);
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10000;
    }
}
