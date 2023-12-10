package cn.qingweico.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author zqw
 * @date 2023/12/9
 * @see reactor.core.publisher.Mono 0-1个元素
 * @see reactor.core.publisher.Flux 0-N个元素
 */
@RestController
public class ReactorController {
    @GetMapping("/mono")
    public Mono<String> mono() {
        return Mono.just("Hello, WebFlux!");
    }

    @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> flux() {
        return Flux.fromStream(IntStream.range(1, 5).mapToObj(e -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return "Flux Data : " + e;
        }));
    }
}
