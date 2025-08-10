package cn.qingweico.controller;

import cn.qingweico.entity.Bill;
import cn.qingweico.model.ApiResponse;
import cn.qingweico.service.BillService;
import cn.qingweico.validate.anno.ParamValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2025/2/8
 */
@Slf4j
@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillService billService;

    @PostMapping(value = "create")

    public ApiResponse<?> create(@ParamValid @RequestBody Bill bill) {
        billService.save(bill);
        return ApiResponse.ok();
    }
}
