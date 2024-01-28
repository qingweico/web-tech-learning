package cn.qingweico.controller;

import cn.qingweico.entity.FriendLink;
import cn.qingweico.entity.model.PostParams;
import cn.qingweico.service.impl.FriendLinkServiceImpl;
import cn.qinwweico.model.ApiResponse;
import cn.qinwweico.model.PagedResult;
import cn.qinwweico.util.Global;
import cn.qinwweico.util.RandomDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author zqw
 * @date 2024/1/25
 */
@RequestMapping("fl")
@RestController
@Slf4j
public class FriendLinkController {
    @Resource
    private FriendLinkServiceImpl friendLinkService;

    @PostMapping("/save")
    public ApiResponse save(@RequestBody FriendLink friendLink) {
        friendLink.setUpdate(new Date());
        friendLink.setCreate(new Date());
        friendLinkService.save(friendLink);
        return ApiResponse.ok();
    }

    @PostMapping("/update")
    public ApiResponse update(@RequestBody FriendLink friendLink) {
        friendLink.setUpdate(new Date());
        friendLinkService.updateById(friendLink);
        return ApiResponse.ok();
    }

    @PostMapping("/searchList")
    public ApiResponse searchList(@RequestBody PostParams postParams) {
        PagedResult result = friendLinkService.searchList(postParams);
        return ApiResponse.ok(result);
    }

    @PostMapping("/delete")
    public ApiResponse batchDelete(@RequestBody List<String> ids) {
        friendLinkService.deleteAll(ids);
        return ApiResponse.ok();
    }

    @PostMapping("/delete/{id}")
    public ApiResponse delete(@PathVariable("id") String linkId) {
        friendLinkService.delete(linkId);
        return ApiResponse.ok();
    }

    @PostMapping("/randomAdd/{quantity}")
    public ApiResponse randomAdd(@PathVariable("quantity") Integer quantity) throws InterruptedException {
        if (quantity != null) {
            CountDownLatch latch = new CountDownLatch(quantity);
            int batch = 10;
            int[] batchArray = Global.splitInteger(quantity, batch);
            long start = System.currentTimeMillis();
            for (int i = 0; i < batch; i++) {
                int frequency = batchArray[i];
                if (frequency != 0) {
                    CompletableFuture.runAsync(() -> {
                        for (int f = 0; f < frequency; f++) {
                            FriendLink fl = new FriendLink();
                            fl.setName(RandomDataUtil.name());
                            fl.setUrl(RandomDataUtil.address());
                            fl.setAvl(RandomDataUtil.zeroOrOne());
                            fl.setCreate(new Date());
                            fl.setUpdate(new Date());
                            friendLinkService.save(fl);
                            latch.countDown();
                        }
                    });
                }
                log.info(">>>>>>>>>>>>>>>>>>>> 第 {} 批次, 添加的数量为: {}", i, frequency);
            }
            latch.await();
            log.info(">>>>>>>>>>>>>>>>>>>> 本次随机添加的数量为: {}, 共耗时 {} ms", quantity, System.currentTimeMillis() - start);
        }
        return ApiResponse.ok();
    }
}
