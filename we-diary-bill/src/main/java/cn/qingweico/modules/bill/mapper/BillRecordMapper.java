package cn.qingweico.modules.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qingweico.modules.bill.entity.BillRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账单记录 Mapper 接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface BillRecordMapper extends BaseMapper<BillRecord> {

    /**
     * 统计近N天的收入和支出
     *
     * @param userId 用户ID（null表示所有用户）
     * @param days   天数
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectStatisticsByDays(@Param("userId") String userId, @Param("days") Integer days);

    /**
     * 按分类统计收入和支出
     *
     * @param userId   用户ID（null表示所有用户）
     * @param billType 账单类型：1-收入，2-支出（null表示全部）
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectStatisticsByCategory(@Param("userId") String userId,
                                                         @Param("billType") Integer billType,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate);

    /**
     * 统计今日收入
     *
     * @param userId 用户ID
     * @return 今日收入金额
     */
    java.math.BigDecimal selectTodayIncome(@Param("userId") String userId);

    /**
     * 统计今日支出
     *
     * @param userId 用户ID
     * @return 今日支出金额
     */
    java.math.BigDecimal selectTodayExpense(@Param("userId") String userId);

    /**
     * 统计总体收入
     *
     * @param userId 用户ID
     * @return 总体收入金额
     */
    java.math.BigDecimal selectTotalIncome(@Param("userId") String userId);

    /**
     * 统计总体支出
     *
     * @param userId 用户ID
     * @return 总体支出金额
     */
    java.math.BigDecimal selectTotalExpense(@Param("userId") String userId);

    /**
     * 近15天收入分析（按日期、分类、支付方式）
     *
     * @param userId 用户ID
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectIncomeAnalysis(@Param("userId") String userId);

    /**
     * 近15天支出分析（按日期、分类、支付方式）
     *
     * @param userId 用户ID
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectExpenseAnalysis(@Param("userId") String userId);

    /**
     * 查询最近的收入记录
     *
     * @param userId 用户ID
     * @param limit  限制条数
     * @return 记录列表
     */
    List<BillRecord> selectRecentIncomeRecords(@Param("userId") String userId, @Param("limit") Integer limit);

    /**
     * 查询最近的支出记录
     *
     * @param userId 用户ID
     * @param limit  限制条数
     * @return 记录列表
     */
    List<BillRecord> selectRecentExpenseRecords(@Param("userId") String userId, @Param("limit") Integer limit);

    /**
     * 按支付方式统计收入和支出
     *
     * @param userId   用户ID（null表示所有用户）
     * @param billType 账单类型：1-收入，2-支出（null表示全部）
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectStatisticsByPayment(@Param("userId") String userId,
                                                         @Param("billType") Integer billType,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate);
}


