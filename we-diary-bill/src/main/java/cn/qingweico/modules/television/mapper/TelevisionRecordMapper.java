package cn.qingweico.modules.television.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qingweico.modules.television.entity.TelevisionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 追剧记录 Mapper 接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface TelevisionRecordMapper extends BaseMapper<TelevisionRecord> {

    /**
     * 查询评分前十的影视
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectTop10ByRating(@Param("userId") String userId);

    /**
     * 统计观看状态数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectWatchStatusStatistics(@Param("userId") String userId);

    /**
     * 查询观看时长前十的影视（结束时间减去开始时间）
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectTop10ByWatchDuration(@Param("userId") String userId);

    /**
     * 统计观影分类前十的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectTop10CategoryStatistics(@Param("userId") String userId);

    /**
     * 按年度统计已看完、正在看的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @param year   年度（null表示所有年度）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectStatisticsByYear(@Param("userId") String userId, @Param("year") Integer year);

    /**
     * 按季度统计已看完、正在看的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @param year   年度
     * @param quarter 季度（1-4，null表示所有季度）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectStatisticsByQuarter(@Param("userId") String userId, @Param("year") Integer year, @Param("quarter") Integer quarter);

    /**
     * 按月统计已看完、正在看的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @param year   年度
     * @param month  月份（1-12，null表示所有月份）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectStatisticsByMonth(@Param("userId") String userId, @Param("year") Integer year, @Param("month") Integer month);

    /**
     * 按周统计已看完、正在看的影视数量
     *
     * @param userId 用户ID（null表示所有用户）
     * @param year   年度
     * @param week   周数（null表示所有周）
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectStatisticsByWeek(@Param("userId") String userId, @Param("year") Integer year, @Param("week") Integer week);
}







