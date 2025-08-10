package cn.qingweico.controller;

import cn.qingweico.service.PdfGenerationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author zqw
 * @date 2025/8/9
 */
@RestController
@RequestMapping("/pdf")
public class PdfController {
    @Resource
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadPdf() throws IOException {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePdf(pdfGenerationService.buildSend());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + UUID.randomUUID() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(new ByteArrayResource(pdfBytes));

        } catch (Exception e) {
            throw new IOException("生成PDF失败", e);
        }
    }

    @GetMapping("/servlet-response-download")
    public void downloadPdf(HttpServletResponse response) throws IOException {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePdf(pdfGenerationService.buildSend());
            // 设置响应头
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + UUID.randomUUID() + ".pdf");
            response.setContentLength(pdfBytes.length);

            // 写入响应流
            OutputStream out = response.getOutputStream();
            out.write(pdfBytes);
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("生成PDF失败: " + e.getMessage());
            throw new IOException("生成PDF失败", e);
        }
    }
}
