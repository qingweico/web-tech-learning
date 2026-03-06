package cn.qingweico.modules.bill.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.common.util.LoginUserUtils;
import cn.qingweico.modules.bill.entity.BillCategory;
import cn.qingweico.modules.bill.service.IBillCategoryService;
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
 * 账单分类控制器
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Api(tags = "账单分类管理")
@RestController
@RequestMapping("/bill/category")
public class BillCategoryController extends SuperController<BillCategory> {

    @Resource
    private IBillCategoryService billCategoryService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "账单分类-分页列表查询")
    @ApiOperation(value = "账单分类-分页列表查询", notes = "账单分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<BillCategory>> queryPageList(BillCategory billCategory,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<BillCategory> queryWrapper = new QueryWrapper<BillCategory>().lambda();
        queryWrapper.eq(billCategory.getCategoryType() != null, BillCategory::getCategoryType, billCategory.getCategoryType())
                .like(StringUtils.isNotBlank(billCategory.getCategoryName()), BillCategory::getCategoryName, billCategory.getCategoryName())
                .orderByAsc(BillCategory::getSortOrder)
                .orderByDesc(BillCategory::getCreated);
        Page<BillCategory> page = new Page<>(pageNo, pageSize);
        IPage<BillCategory> pageList = billCategoryService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 根据类型查询分类列表
     */
    @AutoLog(value = "账单分类-根据类型查询")
    @ApiOperation(value = "账单分类-根据类型查询", notes = "账单分类-根据类型查询")
    @GetMapping(value = "/listByType")
    public Result<List<BillCategory>> listByType(@RequestParam(name = "categoryType") Integer categoryType) {
        String userId = getUserId();
        List<BillCategory> list = billCategoryService.getCategoriesByType(categoryType, userId);
        return Result.ok(list);
    }

    /**
     * 添加
     */
    @AutoLog(value = "账单分类-添加")
    @ApiOperation(value = "账单分类-添加", notes = "账单分类-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BillCategory billCategory) {
        try {
            billCategory.setCreated(new Date());
            billCategory.setLastUpd(new Date());
            billCategory.setCreatedBy(getUserId());
            billCategory.setLastUpdBy(getUserId());
            billCategoryService.save(billCategory);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            log.error("账单分类-添加失败", e);
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 编辑
     */
    @AutoLog(value = "账单分类-编辑")
    @ApiOperation(value = "账单分类-编辑", notes = "账单分类-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BillCategory billCategory) {
        try {
            billCategory.setLastUpd(new Date());
            billCategory.setLastUpdBy(getUserId());
            billCategoryService.updateById(billCategory);
            return Result.ok("编辑成功！");
        } catch (Exception e) {
            log.error("账单分类-编辑失败", e);
            return Result.error("编辑失败：" + e.getMessage());
        }
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "账单分类-通过id删除")
    @ApiOperation(value = "账单分类-通过id删除", notes = "账单分类-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        try {
            billCategoryService.removeById(id);
            return Result.ok("删除成功！");
        } catch (Exception e) {
            log.error("账单分类-删除失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "账单分类-批量删除")
    @ApiOperation(value = "账单分类-批量删除", notes = "账单分类-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Result.error("参数错误");
            }
            billCategoryService.removeByIds(Arrays.asList(ids.split(",")));
            return Result.ok("批量删除成功！");
        } catch (Exception e) {
            log.error("账单分类-批量删除失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
}


