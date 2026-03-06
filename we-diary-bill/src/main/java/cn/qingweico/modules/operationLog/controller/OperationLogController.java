package cn.qingweico.modules.operationLog.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.*;
import cn.qingweico.modules.operationLog.entity.OperationLog;
import cn.qingweico.modules.operationLog.service.IOperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author zqw
 */
@Slf4j
@Api(tags = "操作日志")
@RestController
@RequestMapping("/operationLog/operationLog")
public class OperationLogController {

    @Resource
    private IOperationLogService operationLogService;

    @AutoLog(value = "操作日志-分页列表查询")
    @ApiOperation(value = "操作日志-分页列表查询", notes = "操作日志-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OperationLog>> queryPageList(OperationLog operationLog,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        final LambdaQueryWrapper<OperationLog> lambda = new QueryWrapper<OperationLog>().lambda();
        lambda.eq(StringUtils.isNotBlank(operationLog.getUsername()), OperationLog::getUsername, operationLog.getUsername()).orderByDesc(OperationLog::getCreated);
        if (OperationType.LOGIN.getValue().equals(operationLog.getOperationType())) {
            lambda.eq(OperationLog::getOperationType, operationLog.getOperationType());
        } else {
            lambda.ne(OperationLog::getOperationType, OperationType.LOGIN.getValue());
        }
        Page<OperationLog> page = new Page<OperationLog>(pageNo, pageSize);
        IPage<OperationLog> pageList = operationLogService.page(page, lambda);
        return Result.ok(pageList);
    }

    @AutoLog(value = "操作日志-批量删除")
    @ApiOperation(value = "操作日志-批量删除", notes = "操作日志-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @OperationLogDetail(detail = "操作日志批量删除", level = LogLevelType.ONE,
            operationType = OperationType.DELETE, operationUnit = OperationUnit.UNKNOWN)
    public Result<OperationLog> deleteBatch(@RequestParam(name = "ids") String ids) {
        Result<OperationLog> result = new Result<>();
        if (ids == null || ids.trim().isEmpty()) {
            result.error500("参数错误");
        } else {
            try {
                this.operationLogService.removeByIds(Arrays.asList(ids.split(",")));
                result.success("删除成功!");
            } catch (Exception e) {
                log.info("操作日志-批量删除:{}", e.getMessage(), e);
            }
        }
        return result;
    }
}
