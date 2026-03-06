package cn.qingweico.modules.television.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.modules.television.entity.TelevisionRecord;
import cn.qingweico.modules.television.entity.TelevisionRecordTag;
import cn.qingweico.modules.television.mapper.TelevisionRecordMapper;
import cn.qingweico.modules.television.mapper.TelevisionRecordTagMapper;
import cn.qingweico.modules.television.service.ITelevisionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 追剧记录服务实现类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Service
public class TelevisionRecordServiceImpl extends ServiceImpl<TelevisionRecordMapper, TelevisionRecord> implements ITelevisionRecordService {

    @Resource
    private TelevisionRecordTagMapper recordTagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRecord(TelevisionRecord record) {
        // 保存追剧记录
        boolean success = this.save(record);
        if (success && record.getTagIds() != null && !record.getTagIds().isEmpty()) {
            // 保存标签关联
            saveRecordTags(record.getId(), record.getTagIds(), record.getCreatedBy());
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecord(TelevisionRecord record) {
        // 更新追剧记录
        boolean success = this.updateById(record);
        if (success) {
            // 删除原有标签关联
            LambdaQueryWrapper<TelevisionRecordTag> deleteWrapper = new QueryWrapper<TelevisionRecordTag>().lambda();
            deleteWrapper.eq(TelevisionRecordTag::getRecordId, record.getId());
            recordTagMapper.delete(deleteWrapper);
            // 保存新的标签关联
            if (record.getTagIds() != null && !record.getTagIds().isEmpty()) {
                saveRecordTags(record.getId(), record.getTagIds(), record.getLastUpdBy());
            }
        }
        return success;
    }

    /**
     * 保存追剧记录标签关联
     */
    private void saveRecordTags(String recordId, List<String> tagIds, String userId) {
        Date now = new Date();
        for (String tagId : tagIds) {
            TelevisionRecordTag recordTag = new TelevisionRecordTag();
            recordTag.setRecordId(recordId);
            recordTag.setTagId(tagId);
            recordTag.setCreated(now);
            recordTag.setLastUpd(now);
            recordTag.setCreatedBy(userId);
            recordTag.setLastUpdBy(userId);
            recordTagMapper.insert(recordTag);
        }
    }

    @Override
    public List<Map<String, Object>> getTop10ByRating(String userId) {
        return baseMapper.selectTop10ByRating(userId);
    }

    @Override
    public List<Map<String, Object>> getWatchStatusStatistics(String userId) {
        return baseMapper.selectWatchStatusStatistics(userId);
    }

    @Override
    public List<Map<String, Object>> getTop10ByWatchDuration(String userId) {
        return baseMapper.selectTop10ByWatchDuration(userId);
    }

    @Override
    public List<Map<String, Object>> getTop10CategoryStatistics(String userId) {
        return baseMapper.selectTop10CategoryStatistics(userId);
    }

    @Override
    public List<Map<String, Object>> getStatisticsByYear(String userId, Integer year) {
        return baseMapper.selectStatisticsByYear(userId, year);
    }

    @Override
    public List<Map<String, Object>> getStatisticsByQuarter(String userId, Integer year, Integer quarter) {
        return baseMapper.selectStatisticsByQuarter(userId, year, quarter);
    }

    @Override
    public List<Map<String, Object>> getStatisticsByMonth(String userId, Integer year, Integer month) {
        return baseMapper.selectStatisticsByMonth(userId, year, month);
    }

    @Override
    public List<Map<String, Object>> getStatisticsByWeek(String userId, Integer year, Integer week) {
        return baseMapper.selectStatisticsByWeek(userId, year, week);
    }
}







