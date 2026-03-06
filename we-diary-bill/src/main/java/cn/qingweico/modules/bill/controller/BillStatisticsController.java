package cn.qingweico.modules.bill.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.modules.bill.service.IBillRecordService;
import cn.qingweico.modules.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 账单统计控制器
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Api(tags = "账单统计分析")
@RestController
@RequestMapping("/bill/statistics")
public class BillStatisticsController extends SuperController {

    @Resource
    private IBillRecordService billRecordService;
    @Resource
    private ISysUserService sysUserService;

    /**
     * 判断是否是管理员
     */
    private boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        Set<String> roles = sysUserService.getUserRolesSet(loginUser.getUsername());
        return roles != null && (roles.contains("admin") || roles.contains("管理员"));
    }

    /**
     * 统计近N天的收入和支出
     */
    @AutoLog(value = "账单统计-近N天收支趋势")
    @ApiOperation(value = "账单统计-近N天收支趋势", notes = "账单统计-近N天收支趋势")
    @GetMapping(value = "/trend")
    public Result<List<Map<String, Object>>> getTrendStatistics(
            @RequestParam(name = "days", defaultValue = "15") Integer days,
            @RequestParam(name = "userId", required = false) String userId) {
        try {
            // 如果不是管理员，只能查看自己的统计
            String queryUserId = null;
            if (!isAdmin()) {
                queryUserId = getUserId();
            } else if (userId != null && !userId.isEmpty()) {
                queryUserId = userId;
            }
            
            List<Map<String, Object>> result = billRecordService.getStatisticsByDays(queryUserId, days);
            return Result.ok(result);
        } catch (Exception e) {
            log.error("账单统计-近N天收支趋势失败", e);
            return Result.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 按分类统计收入和支出
     */
    @AutoLog(value = "账单统计-按分类统计")
    @ApiOperation(value = "账单统计-按分类统计", notes = "账单统计-按分类统计")
    @GetMapping(value = "/category")
    public Result<List<Map<String, Object>>> getCategoryStatistics(
            @RequestParam(name = "billType", required = false) Integer billType,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "userId", required = false) String userId) {
        try {
            // 如果不是管理员，只能查看自己的统计
            String queryUserId = null;
            if (!isAdmin()) {
                queryUserId = getUserId();
            } else if (userId != null && !userId.isEmpty()) {
                queryUserId = userId;
            }
            
            List<Map<String, Object>> result = billRecordService.getStatisticsByCategory(queryUserId, billType, startDate, endDate);
            return Result.ok(result);
        } catch (Exception e) {
            log.error("账单统计-按分类统计失败", e);
            return Result.error("统计失败：" + e.getMessage());
        }
    }
}


