package cn.qingweico.common.util;

import com.alibaba.fastjson.JSON;
import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.system.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 */
@Slf4j
public class ResponseUtil {

    public static void returnJson(HttpServletResponse response, Result<String> result) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void returnJson(HttpServletResponse response, R<String> result) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    public static void returnImg(HttpServletResponse response, BufferedImage bufferedImage) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/jpeg");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void returnModelAndView(HttpServletResponse response, String redirectUrl, Result<String> result) {
        ModelMap modelMap = new ModelMap();
        modelMap.putAll(JSON.parseObject(JSON.toJSONString(result)));
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
