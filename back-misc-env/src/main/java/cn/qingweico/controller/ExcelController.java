package cn.qingweico.controller;

import cn.qingweico.constants.FileSuffixConstants;
import cn.qingweico.service.ExcelService;
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
import java.util.UUID;

/**
 * @author zqw
 * @date 2025/11/10
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Resource
    private ExcelService excelService;

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadExcel() throws IOException {
        try {
            byte[] bytes = excelService.create();
            String excelFilename = UUID.randomUUID() + FileSuffixConstants.EXCEL;
            String filenameEncoded = URLEncoder.encode(excelFilename, StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filenameEncoded + "\"; filename*=UTF-8''" + filenameEncoded)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(bytes.length)
                    .body(new ByteArrayResource(bytes));

        } catch (Exception e) {
            throw new IOException("生成Excel失败", e);
        }
    }

}
