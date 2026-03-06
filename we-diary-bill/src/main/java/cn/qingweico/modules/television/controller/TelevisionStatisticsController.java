package cn.qingweico.modules.television.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.modules.television.service.ITelevisionRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 追剧统计控制器
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Api(tags = "追剧统计管理")
@RestController
@RequestMapping("/television/statistics")
public class TelevisionStatisticsController extends SuperController {

    @Resource
    private ITelevisionRecordService recordService;

    /**
     * 查询评分前十的影视
     */
    @AutoLog(value = "追剧统计-评分前十")
    @ApiOperation(value = "追剧统计-评分前十", notes = "追剧统计-评分前十")
    @GetMapping(value = "/top10Rating")
    public Result<List<Map<String, Object>>> getTop10ByRating() {
        List<Map<String, Object>> list = recordService.getTop10ByRating(getUserId());
        return Result.ok(list);
    }

    /**
     * 统计观看状态数量
     */
    @AutoLog(value = "追剧统计-观看状态统计")
    @ApiOperation(value = "追剧统计-观看状态统计", notes = "追剧统计-观看状态统计")
    @GetMapping(value = "/watchStatus")
    public Result<List<Map<String, Object>>> getWatchStatusStatistics() {
        List<Map<String, Object>> list = recordService.getWatchStatusStatistics(getUserId());
        return Result.ok(list);
    }

    /**
     * 查询观看时长前十的影视
     */
    @AutoLog(value = "追剧统计-观看时长前十")
    @ApiOperation(value = "追剧统计-观看时长前十", notes = "追剧统计-观看时长前十")
    @GetMapping(value = "/top10Duration")
    public Result<List<Map<String, Object>>> getTop10ByWatchDuration() {
        List<Map<String, Object>> list = recordService.getTop10ByWatchDuration(getUserId());
        return Result.ok(list);
    }

    /**
     * 统计观影分类前十的影视数量
     */
    @AutoLog(value = "追剧统计-分类前十")
    @ApiOperation(value = "追剧统计-分类前十", notes = "追剧统计-分类前十")
    @GetMapping(value = "/top10Category")
    public Result<List<Map<String, Object>>> getTop10CategoryStatistics() {
        List<Map<String, Object>> list = recordService.getTop10CategoryStatistics(getUserId());
        return Result.ok(list);
    }

    /**
     * 按年度统计已看完、正在看的影视数量
     */
    @AutoLog(value = "追剧统计-年度统计")
    @ApiOperation(value = "追剧统计-年度统计", notes = "追剧统计-年度统计")
    @GetMapping(value = "/byYear")
    public Result<List<Map<String, Object>>> getStatisticsByYear(@RequestParam(required = false) Integer year) {
        List<Map<String, Object>> list = recordService.getStatisticsByYear(getUserId(), year);
        return Result.ok(list);
    }

    /**
     * 按季度统计已看完、正在看的影视数量
     */
    @AutoLog(value = "追剧统计-季度统计")
    @ApiOperation(value = "追剧统计-季度统计", notes = "追剧统计-季度统计")
    @GetMapping(value = "/byQuarter")
    public Result<List<Map<String, Object>>> getStatisticsByQuarter(@RequestParam(required = false) Integer year,
                                                                     @RequestParam(required = false) Integer quarter) {
        List<Map<String, Object>> list = recordService.getStatisticsByQuarter(getUserId(), year, quarter);
        return Result.ok(list);
    }

    /**
     * 按月统计已看完、正在看的影视数量
     */
    @AutoLog(value = "追剧统计-月度统计")
    @ApiOperation(value = "追剧统计-月度统计", notes = "追剧统计-月度统计")
    @GetMapping(value = "/byMonth")
    public Result<List<Map<String, Object>>> getStatisticsByMonth(@RequestParam(required = false) Integer year,
                                                                   @RequestParam(required = false) Integer month) {
        List<Map<String, Object>> list = recordService.getStatisticsByMonth(getUserId(), year, month);
        return Result.ok(list);
    }

    /**
     * 按周统计已看完、正在看的影视数量
     */
    @AutoLog(value = "追剧统计-周统计")
    @ApiOperation(value = "追剧统计-周统计", notes = "追剧统计-周统计")
    @GetMapping(value = "/byWeek")
    public Result<List<Map<String, Object>>> getStatisticsByWeek(@RequestParam(required = false) Integer year,
                                                                 @RequestParam(required = false) Integer week) {
        List<Map<String, Object>> list = recordService.getStatisticsByWeek(getUserId(), year, week);
        return Result.ok(list);
    }
}







