package cn.qingweico.modules.bill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.modules.bill.entity.BillRecord;
import cn.qingweico.modules.bill.mapper.BillRecordMapper;
import cn.qingweico.modules.bill.service.IBillRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账单记录服务实现类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Service
public class BillRecordServiceImpl extends ServiceImpl<BillRecordMapper, BillRecord> implements IBillRecordService {

    @Resource
    private BillRecordMapper billRecordMapper;

    @Override
    public List<Map<String, Object>> getStatisticsByDays(String userId, Integer days) {
        return billRecordMapper.selectStatisticsByDays(userId, days);
    }

    @Override
    public List<Map<String, Object>> getStatisticsByCategory(String userId, Integer billType, String startDate, String endDate) {
        Date start = null;
        Date end = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (startDate != null && !startDate.isEmpty()) {
                start = sdf.parse(startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                end = sdf.parse(endDate);
                // 设置为当天的23:59:59
                end.setTime(end.getTime() + 24 * 60 * 60 * 1000 - 1);
            }
        } catch (ParseException e) {
            log.error("日期解析失败", e);
        }
        return billRecordMapper.selectStatisticsByCategory(userId, billType, start, end);
    }

    @Override
    public BigDecimal getTodayIncome(String userId) {
        BigDecimal result = billRecordMapper.selectTodayIncome(userId);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTodayExpense(String userId) {
        BigDecimal result = billRecordMapper.selectTodayExpense(userId);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalIncome(String userId) {
        BigDecimal result = billRecordMapper.selectTotalIncome(userId);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalExpense(String userId) {
        BigDecimal result = billRecordMapper.selectTotalExpense(userId);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public List<Map<String, Object>> getIncomeAnalysis(String userId) {
        return billRecordMapper.selectIncomeAnalysis(userId);
    }

    @Override
    public List<Map<String, Object>> getExpenseAnalysis(String userId) {
        return billRecordMapper.selectExpenseAnalysis(userId);
    }

    @Override
    public List<BillRecord> getRecentIncomeRecords(String userId, Integer limit) {
        return billRecordMapper.selectRecentIncomeRecords(userId, limit);
    }

    @Override
    public List<BillRecord> getRecentExpenseRecords(String userId, Integer limit) {
        return billRecordMapper.selectRecentExpenseRecords(userId, limit);
    }

    @Override
    public List<Map<String, Object>> getStatisticsByPayment(String userId, Integer billType, String startDate, String endDate) {
        Date start = null;
        Date end = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (startDate != null && !startDate.isEmpty()) {
                start = sdf.parse(startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                end = sdf.parse(endDate);
                // 设置为当天的23:59:59
                end.setTime(end.getTime() + 24 * 60 * 60 * 1000 - 1);
            }
        } catch (ParseException e) {
            log.error("日期解析失败", e);
        }
        return billRecordMapper.selectStatisticsByPayment(userId, billType, start, end);
    }
}


