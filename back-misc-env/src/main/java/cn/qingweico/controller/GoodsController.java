package cn.qingweico.controller;

import cn.qingweico.entity.Goods;
import cn.qingweico.model.ApiResponse;
import cn.qingweico.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zqw
 * @date 2025/2/8
 */
@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public  ApiResponse<List<Goods>> list() {
        List<Goods> list = goodsService.list();
        goodsService.calculateAndSaveAmount();
        return ApiResponse.ok(list);
    }
}
