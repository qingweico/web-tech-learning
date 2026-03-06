package cn.qingweico.modules.system.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.modules.bill.entity.BillRecord;
import cn.qingweico.modules.bill.service.IBillRecordService;
import cn.qingweico.modules.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * @author zhouqingwei
 */
@Slf4j
@RestController
@RequestMapping("/dashboard/analysis")
@Api(tags = "首页")
public class DashboardController extends SuperController {

    @Resource
    private IBillRecordService billRecordService;
    @Resource
    private ISysUserService sysUserService;


    /**
     * 账单统计首页数据查询
     *
     * @return
     */
    @AutoLog(value = "首页-账单统计查询")
    @ApiOperation(value = "首页-账单统计查询", notes = "首页-账单统计查询")
    @PostMapping(value = "/BillAnalysisData")
    public Result<Map<String, Object>> billAnalysisData() {
        try {
            Result<Map<String, Object>> result = new Result<>();
            Map<String, Object> jsonArray = new HashMap<>(16);
            String userId = getUserId();
            CompletableFuture<BigDecimal> todayIncome = CompletableFuture.supplyAsync(() ->
                    billRecordService.getTodayIncome(userId));
            CompletableFuture<BigDecimal> todayExpense = CompletableFuture.supplyAsync(() ->
                    billRecordService.getTodayExpense(userId));
            CompletableFuture<BigDecimal> totalIncome = CompletableFuture.supplyAsync(() ->
                    billRecordService.getTotalIncome(userId));
            CompletableFuture<BigDecimal> totalExpense = CompletableFuture.supplyAsync(() ->
                    billRecordService.getTotalExpense(userId));
            CompletableFuture<List<Map<String, Object>>> incomeAnalysis = CompletableFuture.supplyAsync(() ->
                    billRecordService.getIncomeAnalysis(userId));
            CompletableFuture<List<Map<String, Object>>> expenseAnalysis = CompletableFuture.supplyAsync(() ->
                    billRecordService.getExpenseAnalysis(userId));
            CompletableFuture<List<BillRecord>> recentIncomeRecords = CompletableFuture.supplyAsync(() ->
                    billRecordService.getRecentIncomeRecords(userId, 10));
            CompletableFuture<List<BillRecord>> recentExpenseRecords = CompletableFuture.supplyAsync(() ->
                    billRecordService.getRecentExpenseRecords(userId, 10));

            // 按分类统计（近15天）
            CompletableFuture<List<Map<String, Object>>> incomeCategoryStats = CompletableFuture.supplyAsync(() -> {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DAY_OF_MONTH, -15);
                String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                return billRecordService.getStatisticsByCategory(userId, 1, startDate, endDate);
            });
            CompletableFuture<List<Map<String, Object>>> expenseCategoryStats = CompletableFuture.supplyAsync(() -> {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DAY_OF_MONTH, -15);
                String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                return billRecordService.getStatisticsByCategory(userId, 2, startDate, endDate);
            });

            // 按支付方式统计（近15天）
            CompletableFuture<List<Map<String, Object>>> incomePaymentStats = CompletableFuture.supplyAsync(() -> {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DAY_OF_MONTH, -15);
                String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                return billRecordService.getStatisticsByPayment(userId, 1, startDate, endDate);
            });
            CompletableFuture<List<Map<String, Object>>> expensePaymentStats = CompletableFuture.supplyAsync(() -> {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DAY_OF_MONTH, -15);
                String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                return billRecordService.getStatisticsByPayment(userId, 2, startDate, endDate);
            });

            // 等待所有查询完成
            jsonArray.put("todayIncome", todayIncome.get());
            jsonArray.put("todayExpense", todayExpense.get());
            jsonArray.put("totalIncome", totalIncome.get());
            jsonArray.put("totalExpense", totalExpense.get());
            jsonArray.put("incomeAnalysis", incomeAnalysis.get());
            jsonArray.put("expenseAnalysis", expenseAnalysis.get());
            jsonArray.put("recentIncomeRecords", recentIncomeRecords.get());
            jsonArray.put("recentExpenseRecords", recentExpenseRecords.get());
            jsonArray.put("incomeCategoryStats", incomeCategoryStats.get());
            jsonArray.put("expenseCategoryStats", expenseCategoryStats.get());
            jsonArray.put("incomePaymentStats", incomePaymentStats.get());
            jsonArray.put("expensePaymentStats", expensePaymentStats.get());

            result.setSuccess(true);
            result.setResult(jsonArray);
            return result;
        } catch (Exception e) {
            log.error("账单统计查询失败", e);
            Result<Map<String, Object>> result = new Result<>();
            return result.error500(e.getMessage());
        }
    }

}
