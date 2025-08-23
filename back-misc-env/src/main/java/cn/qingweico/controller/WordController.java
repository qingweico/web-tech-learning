package cn.qingweico.controller;

import cn.qingweico.constants.FileSuffixConstants;
import cn.qingweico.constants.Symbol;
import cn.qingweico.convert.Convert;
import cn.qingweico.service.DocumentService;
import cn.qingweico.service.PdfGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/22
 */
@Slf4j
@RestController
@RequestMapping("/word")
public class WordController {
    @Resource
    private DocumentService documentService;

    @Resource
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadWord() throws IOException {
        try {
            Map<String, Object> dataMap = pdfGenerationService.buildSend();
            String wordFilename = Convert.toString(dataMap.get("title")) + Symbol.DASHED + Convert.toString(dataMap.get("author")) +
                    Symbol.DASHED + Convert.toString(dataMap.get("dynasty")) + FileSuffixConstants.WORD;
            String filenameEncoded = URLEncoder.encode(wordFilename, StandardCharsets.UTF_8);
            byte[] wordBytes = documentService.generateDocument(dataMap);
            log.info("生成word文件 {}...", wordFilename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filenameEncoded + "\"; filename*=UTF-8''" + filenameEncoded)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(wordBytes.length)
                    .body(new ByteArrayResource(wordBytes));

        } catch (Exception e) {
            throw new IOException("生成Word失败", e);
        }
    }
}
