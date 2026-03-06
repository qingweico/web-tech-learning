package cn.qingweico.modules.bill.controller;

import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.AutoLog;
import cn.qingweico.common.system.base.controller.SuperController;
import cn.qingweico.modules.bill.entity.BillRecord;
import cn.qingweico.modules.bill.service.IBillRecordService;
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

/**
 * 账单记录 Controller
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Api(tags = "账单记录管理")
@RestController
@RequestMapping("/bill/record")
public class BillRecordController extends SuperController<BillRecord> {

    @Resource
    private IBillRecordService billRecordService;

    /**
     * 分页列表查询
     */
    @AutoLog(value = "账单记录-分页列表查询")
    @ApiOperation(value = "账单记录-分页列表查询", notes = "账单记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<BillRecord>> queryPageList(BillRecord billRecord,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<BillRecord> queryWrapper = new QueryWrapper<BillRecord>().lambda();
        queryWrapper.eq(BillRecord::getUserId, getUserId());
        queryWrapper.eq(billRecord.getBillType() != null, BillRecord::getBillType, billRecord.getBillType())
                .eq(StringUtils.isNotBlank(billRecord.getCategoryId()), BillRecord::getCategoryId, billRecord.getCategoryId())
                .eq(StringUtils.isNotBlank(billRecord.getPayment()), BillRecord::getPayment, billRecord.getPayment())
                .orderByDesc(BillRecord::getBillTime)
                .orderByDesc(BillRecord::getCreated);
        Page<BillRecord> page = new Page<>(pageNo, pageSize);
        IPage<BillRecord> pageList = billRecordService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     */
    @AutoLog(value = "账单记录-添加")
    @ApiOperation(value = "账单记录-添加", notes = "账单记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BillRecord billRecord) {
        try {
            Date now = new Date();
            billRecord.setUserId(getUserId());
            billRecord.setCreated(now);
            billRecord.setLastUpd(now);
            billRecord.setCreatedBy(getUserId());
            billRecord.setLastUpdBy(getUserId());
            // 如果账单时间为空，则设置为创建时间
            if (billRecord.getBillTime() == null) {
                billRecord.setBillTime(now);
            }
            billRecordService.save(billRecord);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            log.error("账单记录-添加失败", e);
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 编辑
     */
    @AutoLog(value = "账单记录-编辑")
    @ApiOperation(value = "账单记录-编辑", notes = "账单记录-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BillRecord billRecord) {
        try {
            BillRecord existRecord = billRecordService.getById(billRecord.getId());
            if (existRecord == null || !existRecord.getUserId().equals(getUserId())) {
                return Result.error("无权限编辑此账单！");
            }
            billRecord.setLastUpd(new Date());
            billRecord.setLastUpdBy(getUserId());
            billRecordService.updateById(billRecord);
            return Result.ok("编辑成功！");
        } catch (Exception e) {
            log.error("账单记录-编辑失败", e);
            return Result.error("编辑失败：" + e.getMessage());
        }
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "账单记录-通过id删除")
    @ApiOperation(value = "账单记录-通过id删除", notes = "账单记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        try {
            BillRecord existRecord = billRecordService.getById(id);
            if (existRecord == null || !existRecord.getUserId().equals(getUserId())) {
                return Result.error("无权限删除此账单！");
            }
            billRecordService.removeById(id);
            return Result.ok("删除成功！");
        } catch (Exception e) {
            log.error("账单记录-删除失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "账单记录-批量删除")
    @ApiOperation(value = "账单记录-批量删除", notes = "账单记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        try {
            if (StringUtils.isBlank(ids)) {
                return Result.error("参数错误");
            }
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                BillRecord existRecord = billRecordService.getById(id);
                if (existRecord == null || !existRecord.getUserId().equals(getUserId())) {
                    return Result.error("无权限删除此账单！");
                }
            }
            billRecordService.removeByIds(Arrays.asList(ids.split(",")));
            return Result.ok("批量删除成功！");
        } catch (Exception e) {
            log.error("账单记录-批量删除失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
}


