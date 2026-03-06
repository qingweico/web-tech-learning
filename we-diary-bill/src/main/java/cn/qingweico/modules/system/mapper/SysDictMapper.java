package cn.qingweico.modules.system.mapper;

import java.util.List;

import cn.qingweico.common.system.vo.DictModel;
import cn.qingweico.modules.system.entity.SysDict;
import cn.qingweico.modules.system.model.DuplicateCheckVo;
import cn.qingweico.modules.system.model.TreeSelectModel;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author zhouqingwei
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * 重复检查SQL
     */
    Long duplicateCheckCountSql(DuplicateCheckVo duplicateCheckVo);

    Long duplicateCheckCountSqlNoDataId(DuplicateCheckVo duplicateCheckVo);

    List<DictModel> queryDictItemsByCode(@Param("code") String code);

    List<DictModel> queryTableDictItemsByCode(@Param("table") String table, @Param("text") String text, @Param("code") String code);

    List<DictModel> queryTableDictItemsByCodeAndFilter(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("filterSql") String filterSql);

    String queryDictTextByKey(@Param("code") String code, @Param("key") String key);

    String queryTableDictTextByKey(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("key") String key);

    /**
     * 查询所有部门 作为字典信息
     *
     * @return [{value: id, text: departName}]
     */
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 查询所有用户  作为字典信息
     *
     * @return [{value: username, text: realname}]
     */
    List<DictModel> queryAllUserBackDictModel();

    /**
     * 通过关键字查询出字典表
     */
    List<DictModel> queryTableDictItems(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("keyword") String keyword);

    /**
     * 根据表名、显示字段名、存储字段名 查询树
     */
    List<TreeSelectModel> queryTreeList(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("pidField") String pidField, @Param("pid") String pid, @Param("hasChildField") String hasChildField);

}
