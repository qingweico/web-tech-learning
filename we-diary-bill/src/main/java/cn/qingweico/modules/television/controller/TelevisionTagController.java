package cn.qingweico.modules.television.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.modules.television.entity.TelevisionTag;
import cn.qingweico.modules.television.service.ITelevisionTagService;
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

/**
 * 观影标签控制器
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Api(tags = "观影标签管理")
@RestController
@RequestMapping("/television/tag")
public class TelevisionTagController extends SuperController<TelevisionTag> {

    @Resource
    private ITelevisionTagService tagService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "观影标签-分页列表查询")
    @ApiOperation(value = "观影标签-分页列表查询", notes = "观影标签-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TelevisionTag>> queryPageList(TelevisionTag tag,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<TelevisionTag> queryWrapper = new QueryWrapper<TelevisionTag>().lambda();
        queryWrapper.like(StringUtils.isNotBlank(tag.getName()), TelevisionTag::getName, tag.getName())
                .orderByAsc(TelevisionTag::getSort)
                .orderByDesc(TelevisionTag::getCreated);
        Page<TelevisionTag> page = new Page<>(pageNo, pageSize);
        IPage<TelevisionTag> pageList = tagService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 获取所有标签列表
     */
    @AutoLog(value = "观影标签-获取所有标签")
    @ApiOperation(value = "观影标签-获取所有标签", notes = "观影标签-获取所有标签")
    @GetMapping(value = "/all")
    public Result<List<TelevisionTag>> getAllTags() {
        List<TelevisionTag> list = tagService.getAllTags(null);
        return Result.ok(list);
    }

    /**
     * 添加
     */
    @AutoLog(value = "观影标签-添加")
    @ApiOperation(value = "观影标签-添加", notes = "观影标签-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TelevisionTag tag) {
        try {
            Date now = new Date();
            tag.setCreated(now);
            tag.setLastUpd(now);
            tag.setCreatedBy(getUserId());
            tag.setLastUpdBy(getUserId());
            tagService.save(tag);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            log.error("观影标签-添加失败", e);
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 编辑
     */
    @AutoLog(value = "观影标签-编辑")
    @ApiOperation(value = "观影标签-编辑", notes = "观影标签-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TelevisionTag tag) {
        try {
            tag.setLastUpd(new Date());
            tag.setLastUpdBy(getUserId());
            tagService.updateById(tag);
            return Result.ok("编辑成功！");
        } catch (Exception e) {
            log.error("观影标签-编辑失败", e);
            return Result.error("编辑失败：" + e.getMessage());
        }
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "观影标签-通过id删除")
    @ApiOperation(value = "观影标签-通过id删除", notes = "观影标签-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        try {
            tagService.removeById(id);
            return Result.ok("删除成功！");
        } catch (Exception e) {
            log.error("观影标签-删除失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "观影标签-批量删除")
    @ApiOperation(value = "观影标签-批量删除", notes = "观影标签-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Result.error("参数错误");
            }
            tagService.removeByIds(Arrays.asList(ids.split(",")));
            return Result.ok("批量删除成功！");
        } catch (Exception e) {
            log.error("观影标签-批量删除失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
}


