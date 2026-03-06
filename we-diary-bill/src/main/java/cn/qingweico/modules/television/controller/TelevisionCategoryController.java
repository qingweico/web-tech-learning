package cn.qingweico.modules.television.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.modules.television.entity.TelevisionCategory;
import cn.qingweico.modules.television.service.ITelevisionCategoryService;
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
 * 观影分类控制器
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Api(tags = "观影分类管理")
@RestController
@RequestMapping("/television/category")
public class TelevisionCategoryController extends SuperController<TelevisionCategory> {

    @Resource
    private ITelevisionCategoryService categoryService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "观影分类-分页列表查询")
    @ApiOperation(value = "观影分类-分页列表查询", notes = "观影分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TelevisionCategory>> queryPageList(TelevisionCategory category,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<TelevisionCategory> queryWrapper = new QueryWrapper<TelevisionCategory>().lambda();
        queryWrapper.like(StringUtils.isNotBlank(category.getName()), TelevisionCategory::getName, category.getName())
                .orderByAsc(TelevisionCategory::getSort)
                .orderByDesc(TelevisionCategory::getCreated);
        Page<TelevisionCategory> page = new Page<>(pageNo, pageSize);
        IPage<TelevisionCategory> pageList = categoryService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 获取所有分类列表
     */
    @AutoLog(value = "观影分类-获取所有分类")
    @ApiOperation(value = "观影分类-获取所有分类", notes = "观影分类-获取所有分类")
    @GetMapping(value = "/all")
    public Result<List<TelevisionCategory>> getAllCategories() {
        List<TelevisionCategory> list = categoryService.getAllCategories(null);
        return Result.ok(list);
    }

    /**
     * 添加
     */
    @AutoLog(value = "观影分类-添加")
    @ApiOperation(value = "观影分类-添加", notes = "观影分类-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TelevisionCategory category) {
        try {
            Date now = new Date();
            category.setCreated(now);
            category.setLastUpd(now);
            category.setCreatedBy(getUserId());
            category.setLastUpdBy(getUserId());
            categoryService.save(category);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            log.error("观影分类-添加失败", e);
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 编辑
     */
    @AutoLog(value = "观影分类-编辑")
    @ApiOperation(value = "观影分类-编辑", notes = "观影分类-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TelevisionCategory category) {
        try {
            category.setLastUpd(new Date());
            category.setLastUpdBy(getUserId());
            categoryService.updateById(category);
            return Result.ok("编辑成功！");
        } catch (Exception e) {
            log.error("观影分类-编辑失败", e);
            return Result.error("编辑失败：" + e.getMessage());
        }
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "观影分类-通过id删除")
    @ApiOperation(value = "观影分类-通过id删除", notes = "观影分类-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        try {
            categoryService.removeById(id);
            return Result.ok("删除成功！");
        } catch (Exception e) {
            log.error("观影分类-删除失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "观影分类-批量删除")
    @ApiOperation(value = "观影分类-批量删除", notes = "观影分类-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Result.error("参数错误");
            }
            categoryService.removeByIds(Arrays.asList(ids.split(",")));
            return Result.ok("批量删除成功！");
        } catch (Exception e) {
            log.error("观影分类-批量删除失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
}


