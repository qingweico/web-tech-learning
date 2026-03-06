package cn.qingweico.modules.bill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.modules.bill.entity.BillRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账单记录服务接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface IBillRecordService extends IService<BillRecord> {

    /**
     * 统计近N天的收入和支出
     *
     * @param userId 用户ID（null表示所有用户，管理员可查看）
     * @param days   天数
     * @return 统计数据列表
     */
    List<Map<String, Object>> getStatisticsByDays(String userId, Integer days);

    /**
     * 按分类统计收入和支出
     *
     * @param userId   用户ID（null表示所有用户，管理员可查看）
     * @param billType 账单类型：1-收入，2-支出（null表示全部）
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计数据列表
     */
    List<Map<String, Object>> getStatisticsByCategory(String userId, Integer billType, String startDate, String endDate);

    /**
     * 统计今日收入
     */
    BigDecimal getTodayIncome(String userId);

    /**
     * 统计今日支出
     */
    BigDecimal getTodayExpense(String userId);

    /**
     * 统计总体收入
     */
    BigDecimal getTotalIncome(String userId);

    /**
     * 统计总体支出
     */
    BigDecimal getTotalExpense(String userId);

    /**
     * 近15天收入分析
     */
    List<Map<String, Object>> getIncomeAnalysis(String userId);

    /**
     * 近15天支出分析
     */
    List<Map<String, Object>> getExpenseAnalysis(String userId);

    /**
     * 查询最近的收入记录
     */
    List<BillRecord> getRecentIncomeRecords(String userId, Integer limit);

    /**
     * 查询最近的支出记录
     */
    List<BillRecord> getRecentExpenseRecords(String userId, Integer limit);

    /**
     * 按支付方式统计收入和支出
     *
     * @param userId   用户ID（null表示所有用户，管理员可查看）
     * @param billType 账单类型：1-收入，2-支出（null表示全部）
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计数据列表
     */
    List<Map<String, Object>> getStatisticsByPayment(String userId, Integer billType, String startDate, String endDate);
}


