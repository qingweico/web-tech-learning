package cn.qingweico.common.system.base.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cn.qingweico.common.system.base.entity.SuperEntity;
import cn.qingweico.common.system.base.service.ISuperService;
import cn.qingweico.common.system.query.QueryGenerator;
import cn.qingweico.common.system.util.R;
import cn.qingweico.common.system.validators.ValidatorUtils;
import cn.qingweico.common.system.validators.group.AddGroup;
import cn.qingweico.common.system.validators.group.UpdateGroup;
import cn.qingweico.common.util.DateUtil;
import cn.qingweico.common.util.excel.annotation.Excel;
import cn.qingweico.common.util.excel.annotation.ExcelTarget;
import cn.qingweico.common.util.excel.entity.params.ExportParams;
import cn.qingweico.common.util.excel.entity.params.ImportParams;
import cn.qingweico.common.util.excel.entity.result.ExcelImportResult;
import cn.qingweico.common.util.excel.util.ExcelImportUtil;
import cn.qingweico.common.util.excel.util.PoiPublicUtil;
import cn.qingweico.common.util.excel.web.entity.vo.NormalExcelConstants;
import cn.qingweico.common.util.excel.web.view.EasypoiSingleExcelView;
import cn.qingweico.common.util.excel.web.view.PoiBaseView;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 通用公共controller
 */
@Slf4j
public class CrudController<BaseService extends ISuperService<Entity>, Entity extends SuperEntity> extends SuperController<Entity> {
    @Resource
    protected BaseService baseService;

    /**
     * 列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询接口", notes = "分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "params", value = "参数信息", required = true, paramType = "query"),
    })
    public R<IPage<Entity>> page(Entity entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest req) {
        QueryWrapper<Entity> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        Page<Entity> page = new Page<>(pageNo, pageSize);
        IPage<Entity> pageList = baseService.page(page, queryWrapper);
        log.debug("查询当前页: " + pageList.getCurrent());
        log.debug("查询当前页数量: " + pageList.getSize());
        log.debug("查询结果数量: " + pageList.getRecords().size());
        log.debug("数据总数: " + pageList.getTotal());
        return R.ok(pageList);
    }


    /**
     * 列表
     */
    @GetMapping("/pageList")
    @ApiOperation(value = "分页查询接口", notes = "分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "params", value = "参数信息", required = true, paramType = "query"),
    })
    public R<IPage<Entity>> pageList(Entity entity, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<Entity> queryWrapper = getQueryWraper(entity);
        Page<Entity> page = new Page<>(pageNo, pageSize);
        IPage<Entity> pageList = baseService.page(page, queryWrapper);
        log.debug("查询当前页: " + pageList.getCurrent());
        log.debug("查询当前页数量: " + pageList.getSize());
        log.debug("查询结果数量: " + pageList.getRecords().size());
        log.debug("数据总数: " + pageList.getTotal());
        return R.ok(pageList);
    }

    /**
     * 信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取详细信息", notes = "根据ID获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "id", value = "ID", required = true, paramType = "path"),
    })
    public R getById(@PathVariable("id") String id) {
        Entity entity = baseService.getById(id);
        return new R<>(entity);
    }

    /**
     * 保存
     */
    @PostMapping({"", "/add"})
    @ApiOperation(value = "添加信息", notes = "添加信息")
    public R save(@RequestBody Entity entity) {
        ValidatorUtils.validateEntity(entity, AddGroup.class);
        return new R<>(baseService.save(entity));
    }

    /**
     * 修改
     */
    @PutMapping({"", "/edit"})
    @ApiOperation(value = "根据ID修改信息", notes = "根据ID修改信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "id", value = "ID", required = true, paramType = "path"),
    })
    public R<Boolean> update(@RequestBody Entity entity) {
        ValidatorUtils.validateEntity(entity, UpdateGroup.class);
        return R.ok(baseService.updateById(entity));
    }


    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "根据IDS删除信息", notes = "根据IDS删除信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "id", value = "ID", required = true, paramType = "path"),
    })
    public R deleteById(@RequestParam(name = "id") String id) {
        List<Serializable> idList = Arrays.asList(id.split(","));
        return R.ok(baseService.removeByIds(idList));
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteBatch")
    @ApiOperation(value = "根据IDS删除信息", notes = "根据IDS删除信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "ids", value = "ID", required = true, paramType = "path"),
    })
    public R deleteBatch(@RequestParam String ids) {
        List<Serializable> idList = Lists.newArrayList(ids.split(","));
        return R.ok(baseService.removeByIds(idList));
    }

    @ApiOperation(value = "获取所有列表信息", notes = "获取所有列表信息")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Entity> all() {
        return baseService.list();
    }


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "params", value = "参数信息", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public R<List<Entity>> list(Entity entity) throws Exception {
        QueryWrapper<Entity> queryWrapper = new QueryWrapper(entity);
        return R.ok(baseService.list(queryWrapper));
    }


    @ApiOperation(value = "导出excle", notes = "导出excle")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "params", value = "参数信息", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    public void exportXls(Entity entity, String targetFileName, String field, String selections, HttpServletRequest req, HttpServletResponse res) {
        Class<Entity> entityClass = getSuperClass(getClass());

        QueryWrapper<Entity> queryWrapper = QueryGenerator.initQueryWrapper(entity, req.getParameterMap());
        /*.select(entityClass, t -> !t.getColumn().toLowerCase().contains("id"))*/

        if (StringUtils.isNotEmpty(selections)) {
            List<Field> pkFields = Arrays.stream(PoiPublicUtil.getClassFields(entityClass)).filter(f -> f.isAnnotationPresent(TableId.class)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(pkFields)) {
                Field keyF = pkFields.get(0);
                queryWrapper.and(v -> v.in(keyF.isAnnotationPresent(TableField.class) ? keyF.getAnnotation(TableField.class).value() : com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(keyF.getName()), Lists.newArrayList(selections.split(","))));
            }
        }

        //Step.2 AutoPoi 导出Excel
//        ModelAndView mv = new ModelAndView(new EasypoiSingleExcelView());
        ModelMap mv = new ModelMap();
        List<Entity> pageList = baseService.list(queryWrapper);

        ExcelTarget excelTarget = entityClass.getAnnotation(ExcelTarget.class);
        String fileName = excelTarget != null ? excelTarget.name().replace("$date.long", DateUtil.getDay()) : targetFileName;
        //导出文件名称
        mv.put(NormalExcelConstants.FILE_NAME, fileName);
        mv.put(NormalExcelConstants.CLASS, entityClass);
        ExportParams exportParams = new ExportParams(fileName, fileName);
        if (StringUtils.isNotEmpty(field)) {
            exportParams.setInclusions(field.split(","));
        }
        mv.put(NormalExcelConstants.PARAMS, exportParams);
        mv.put(NormalExcelConstants.DATA_LIST, pageList);
        log.info("导出成功, 表名称: {},文件名称: {}", entityClass.getSimpleName(), fileName);
        PoiBaseView.render(mv, req, res, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }


    @ApiOperation(value = "导出excle模板", notes = "导出excle模板")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "params", value = "参数信息", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/exportXlsTemplate", method = RequestMethod.GET)
    public ModelAndView exportXlsTemplate(Entity entity, String targetFileName, String field, HttpServletRequest req) {
        Class<Entity> entityClass = getSuperClass(getClass());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new EasypoiSingleExcelView());

        ExcelTarget excelTarget = entityClass.getAnnotation(ExcelTarget.class);
        String fileName = (excelTarget != null ? excelTarget.name().replace("$date.long", DateUtil.getDay()) : targetFileName) + "模板";
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, fileName);
        mv.addObject(NormalExcelConstants.CLASS, entityClass);
        ExportParams exportParams = new ExportParams(fileName, fileName);
        //全局过滤字段
        exportParams.setExclusions(new String[]{"id", "created", "createdBy", "lastUpd", "lastUpdBy", "createDate", "modifyDate"});

        if (StringUtils.isNotEmpty(field)) {
            exportParams.setInclusions(field.split(","));
        }
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, Collections.emptyList());
        log.info("导出模板成功, 表名称: {},文件名称: {}", entityClass.getSimpleName(), fileName);
        return mv;
    }


    @ApiOperation(value = "导入excle", notes = "导入excle")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "params", value = "参数信息", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/importExcel", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public R<?> importExcel(String field, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<Entity> entityClass = getSuperClass(getClass());
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String result = "";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            /**
             * 获取上传文件对象
             */
            MultipartFile file = entity.getValue();
            try (InputStream inputStream = file.getInputStream()) {
                ImportParams importParams = new ImportParams()
                        .setTitleRows(1)
                        .setHeadRows(1)
                        .setNeedVerify(true)
                        .setNeedSave(true);

                if (StringUtils.isNotEmpty(field)) {
                    Set<String> imprortFieldSets = Sets.newLinkedHashSet();

                    Arrays.stream(field.split(",")).forEach(i -> {
                                List<Field> fieldList = Arrays.stream(PoiPublicUtil.getClassFields(entityClass)).filter(f ->
                                        f.getName().equals(i)).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(fieldList) && fieldList.get(0).isAnnotationPresent(Excel.class)) {
                                    imprortFieldSets.add(fieldList.get(0).getAnnotation(Excel.class).name());
                                }
                            }
                    );
                    importParams.setImportFields(imprortFieldSets.toArray(new String[0]));
                }

                ExcelImportResult<Entity> dataList = ExcelImportUtil.importExcelMore(inputStream, entityClass, importParams);


                List<Entity> listDatas = convertDataList(dataList.getList());
                log.info("导入excel原始数据为: 【{}】", listDatas);
                if (!CollectionUtils.isEmpty(listDatas)) {
                    baseService.saveBatch(listDatas);
                    result = "文件导入成功数据行数: " + listDatas.size();
                }
                if (dataList.isVerfiyFail()) {
                    result += ";导入失败数据行数: " + dataList.getFailList().size();
                }
                return R.ok(result);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return R.error("文件导入失败:" + e.getMessage());
            }
        }
        return R.error("文件导入失败！");
    }

    /**
     * 处理excel解析原始数据
     *
     * @param list
     * @return
     */
    public List<Entity> convertDataList(List<Entity> list) {
        return list;
    }


    /**
     * 获取查询封装wrapper
     *
     * @return
     */
    public QueryWrapper<Entity> getQueryWraper(Entity entity) {
        return QueryGenerator.initQueryWrapper(entity, getRequest().getParameterMap());
    }

}
