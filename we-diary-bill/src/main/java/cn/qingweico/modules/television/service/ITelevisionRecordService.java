package cn.qingweico.modules.television.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.modules.television.entity.TelevisionRecord;

import java.util.List;
import java.util.Map;

/**
 * 追剧记录服务接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface ITelevisionRecordService extends IService<TelevisionRecord> {

    /**
     * 保存追剧记录（包含标签关联）
     *
     * @param record 追剧记录
     * @return 是否成功
     */
    boolean saveRecord(TelevisionRecord record);

    /**
     * 更新追剧记录（包含标签关联）
     *
     * @param record 追剧记录
     * @return 是否成功
     */
    boolean updateRecord(TelevisionRecord record);

    /**
     * 查询评分前十的影视
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getTop10ByRating(String userId);

    /**
     * 统计观看状态数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getWatchStatusStatistics(String userId);

    /**
     * 查询观看时长前十的影视
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getTop10ByWatchDuration(String userId);

    /**
     * 统计观影分类前十的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getTop10CategoryStatistics(String userId);

    /**
     * 按年度统计已看完、正在看的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @param year   年度（null表示所有年度）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getStatisticsByYear(String userId, Integer year);

    /**
     * 按季度统计已看完、正在看的影视数量
     *
     * @param userId  用户ID（null表示所有用户）
     * @param year    年度
     * @param quarter 季度（1-4，null表示所有季度）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getStatisticsByQuarter(String userId, Integer year, Integer quarter);

    /**
     * 按月统计已看完、正在看的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @param year   年度
     * @param month  月份（1-12，null表示所有月份）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getStatisticsByMonth(String userId, Integer year, Integer month);

    /**
     * 按周统计已看完、正在看的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @param year   年度
     * @param week   周数（null表示所有周）
     * @return 统计数据列表
     */
    List<Map<String, Object>> getStatisticsByWeek(String userId, Integer year, Integer week);
}







