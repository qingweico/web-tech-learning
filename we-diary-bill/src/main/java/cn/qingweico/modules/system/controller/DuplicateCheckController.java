package cn.qingweico.modules.system.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.modules.system.mapper.SysDictMapper;
import cn.qingweico.modules.system.model.DuplicateCheckVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhouqingwei
 */
@Slf4j
@RestController
@RequestMapping("/sys/duplicate")
@Api(tags = "重复校验")
public class DuplicateCheckController {
    @Resource
    SysDictMapper sysDictMapper;
    /**
     * 校验数据是否在系统中是否存在
     *
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ApiOperation("重复校验接口")
    public Result<Object> doDuplicateCheck(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
        Long num;

        log.info("----duplicate check------: {}", duplicateCheckVo.toString());
        if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
            // [2].编辑页面校验
            num = sysDictMapper.duplicateCheckCountSql(duplicateCheckVo);
        } else {
            // [1].添加页面校验
            num = sysDictMapper.duplicateCheckCountSqlNoDataId(duplicateCheckVo);
        }

        if (num == null || num == 0) {
            // 该值可用
            return Result.ok("该值可用！");
        } else {
            // 该值不可用
            log.info("该值不可用, 系统中已存在！");
            return Result.error("该值不可用, 系统中已存在！");
        }
    }
}
