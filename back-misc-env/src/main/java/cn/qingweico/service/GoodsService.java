package cn.qingweico.service;

import cn.qingweico.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zqw
 * @date 2023/6/22
 */
public interface GoodsService extends IService<Goods> {

    @Async
    void calculateAndSaveAmount();
}
