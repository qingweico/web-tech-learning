package cn.qingweico.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zqw
 * @date 2023/12/9
 */
@WebServlet("/sse")
public class SseServlet extends HttpServlet {
    private static final long serialVersionUID = 7805210199097658356L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        int count = 5;
        for (int i = 0; i < count; i++) {
            resp.getWriter().write("data:sse\n");
            resp.getWriter().write("data:" + i + "\n\n");
            resp.getWriter().flush();
        }
        super.doGet(req, resp);
    }
}
