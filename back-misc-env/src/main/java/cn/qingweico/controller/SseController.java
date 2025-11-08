package cn.qingweico.controller;

import cn.qingweico.service.PdfGenerationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * @author zqw
 * @date 2025/8/9
 */
@RestController
@RequestMapping("/sse")
public class SseController {
    @Resource
    private ExecutorService executorService;
    @Resource
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/stream")
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(60_000L);

        executorService.execute(() -> {
            try {
                Collection<Object> coll = pdfGenerationService.buildSend().values();
                for (Object row : coll) {
                    emitter.send(SseEmitter.event()
                            .data(row));
                    Thread.sleep(1000);
                }

                emitter.complete();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }


}
