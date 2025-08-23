package cn.qingweico.controller;

import cn.qingweico.constants.FileSuffixConstants;
import cn.qingweico.constants.Symbol;
import cn.qingweico.convert.Convert;
import cn.qingweico.service.PdfGenerationService;
import cn.qingweico.supplier.RandomDataGenerator;
import lombok.extern.slf4j.Slf4j;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/9
 */
@Slf4j
@RestController
@RequestMapping("/pdf")
public class PdfController {
    @Resource
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadPdf() throws IOException {
        try {
            Map<String, Object> pdfMap = pdfGenerationService.buildSend();
            String pdgFilename = Convert.toString(pdfMap.get("title")) + Symbol.DASHED + Convert.toString(pdfMap.get("author")) +
                    Symbol.DASHED + Convert.toString(pdfMap.get("dynasty")) + FileSuffixConstants.PDF;
            byte[] pdfBytes = pdfGenerationService.generatePdf(pdfMap);
            HttpHeaders headers = new HttpHeaders();
            log.info("生成pdf文件 {}...", pdgFilename);
            String filenameEncoded = URLEncoder.encode(pdgFilename, StandardCharsets.UTF_8);
            // filename 使用引号包裹, 避免解析歧义(如果文件名中含有特殊字符, 比如空格,可能会导致解析错误)
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filenameEncoded + "\"; filename*=UTF-8''" + filenameEncoded);
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);

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
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            String filenameEncoded = URLEncoder.encode(RandomDataGenerator.name(true) + FileSuffixConstants.PDF, StandardCharsets.UTF_8);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filenameEncoded + "\"; filename*=UTF-8''" + filenameEncoded);
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
