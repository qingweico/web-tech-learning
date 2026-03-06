package cn.qingweico.modules.television.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.modules.television.entity.TelevisionRecord;
import cn.qingweico.modules.television.entity.TelevisionRecordTag;
import cn.qingweico.modules.television.mapper.TelevisionRecordTagMapper;
import cn.qingweico.modules.television.service.ITelevisionRecordService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 追剧记录控制器
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Api(tags = "追剧记录管理")
@RestController
@RequestMapping("/television/record")
public class TelevisionRecordController extends SuperController<TelevisionRecord> {

    @Resource
    private ITelevisionRecordService recordService;

    @Resource
    private TelevisionRecordTagMapper recordTagMapper;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "追剧记录-分页列表查询")
    @ApiOperation(value = "追剧记录-分页列表查询", notes = "追剧记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TelevisionRecord>> queryPageList(TelevisionRecord record,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<TelevisionRecord> queryWrapper = new QueryWrapper<TelevisionRecord>().lambda();
        queryWrapper.eq(record.getWatchStatus() != null, TelevisionRecord::getWatchStatus, record.getWatchStatus())
                .eq(StringUtils.isNotBlank(record.getCategoryId()), TelevisionRecord::getCategoryId, record.getCategoryId())
                .like(StringUtils.isNotBlank(record.getName()), TelevisionRecord::getName, record.getName())
                .orderByDesc(TelevisionRecord::getCreated);
        Page<TelevisionRecord> page = new Page<>(pageNo, pageSize);
        IPage<TelevisionRecord> pageList = recordService.page(page, queryWrapper);
        
        // 填充分类名称和标签名称
        fillCategoryAndTags(pageList.getRecords());
        
        return Result.ok(pageList);
    }

    /**
     * 填充分类名称和标签名称
     */
    private void fillCategoryAndTags(List<TelevisionRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        for (TelevisionRecord record : records) {
            // 查询标签
            LambdaQueryWrapper<TelevisionRecordTag> tagWrapper = new QueryWrapper<TelevisionRecordTag>().lambda();
            tagWrapper.eq(TelevisionRecordTag::getRecordId, record.getId());
            List<TelevisionRecordTag> recordTags = recordTagMapper.selectList(tagWrapper);
            if (recordTags != null && !recordTags.isEmpty()) {
                List<String> tagIds = recordTags.stream().map(TelevisionRecordTag::getTagId).collect(Collectors.toList());
                record.setTagIds(tagIds);
            }
        }
    }

    /**
     * 添加
     */
    @AutoLog(value = "追剧记录-添加")
    @ApiOperation(value = "追剧记录-添加", notes = "追剧记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TelevisionRecord record) {
        try {
            Date now = new Date();
            record.setCreated(now);
            record.setLastUpd(now);
            record.setCreatedBy(getUserId());
            record.setLastUpdBy(getUserId());
            recordService.saveRecord(record);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            log.error("追剧记录-添加失败", e);
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 编辑
     */
    @AutoLog(value = "追剧记录-编辑")
    @ApiOperation(value = "追剧记录-编辑", notes = "追剧记录-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TelevisionRecord record) {
        try {
            TelevisionRecord existRecord = recordService.getById(record.getId());
            if (existRecord == null || !existRecord.getCreatedBy().equals(getUserId())) {
                return Result.error("无权限编辑此记录！");
            }
            record.setLastUpd(new Date());
            record.setLastUpdBy(getUserId());
            recordService.updateRecord(record);
            return Result.ok("编辑成功！");
        } catch (Exception e) {
            log.error("追剧记录-编辑失败", e);
            return Result.error("编辑失败：" + e.getMessage());
        }
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "追剧记录-通过id删除")
    @ApiOperation(value = "追剧记录-通过id删除", notes = "追剧记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        try {
            TelevisionRecord existRecord = recordService.getById(id);
            if (existRecord == null || !existRecord.getCreatedBy().equals(getUserId())) {
                return Result.error("无权限删除此记录！");
            }
            // 删除标签关联
            LambdaQueryWrapper<TelevisionRecordTag> tagWrapper = new QueryWrapper<TelevisionRecordTag>().lambda();
            tagWrapper.eq(TelevisionRecordTag::getRecordId, id);
            recordTagMapper.delete(tagWrapper);
            // 删除记录
            recordService.removeById(id);
            return Result.ok("删除成功！");
        } catch (Exception e) {
            log.error("追剧记录-删除失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "追剧记录-批量删除")
    @ApiOperation(value = "追剧记录-批量删除", notes = "追剧记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Result.error("参数错误");
            }
            String[] idArray = ids.split(",");
            String userId = getUserId();
            for (String id : idArray) {
                TelevisionRecord existRecord = recordService.getById(id);
                if (existRecord == null || !existRecord.getCreatedBy().equals(userId)) {
                    return Result.error("无权限删除此记录！");
                }
                // 删除标签关联
                LambdaQueryWrapper<TelevisionRecordTag> tagWrapper = new QueryWrapper<TelevisionRecordTag>().lambda();
                tagWrapper.eq(TelevisionRecordTag::getRecordId, id);
                recordTagMapper.delete(tagWrapper);
            }
            recordService.removeByIds(Arrays.asList(idArray));
            return Result.ok("批量删除成功！");
        } catch (Exception e) {
            log.error("追剧记录-批量删除失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
}







