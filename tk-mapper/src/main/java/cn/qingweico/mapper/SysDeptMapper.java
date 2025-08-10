package cn.qingweico.mapper;

import cn.qingweico.entity.SysDept;
import cn.qingweico.repository.SysDeptExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author zqw
 * @date 2025/7/18
 */
@Repository
public interface SysDeptMapper {
    /**
     * 根据条件统计
     *
     * @param example 查询条件 若此条件为null则查询表内总数据量
     * @return 统计数量
     */
    long countByExample(SysDeptExample example);

    /**
     * 根据条件删除
     *
     * @param example 查询条件 若此条件为null则删除所有数据
     * @return 影响记录数
     */
    int deleteByExample(SysDeptExample example);

    /**
     * 根据主键删除
     *
     * @param deptNo 主键
     * @return 影响记录数
     */
    int deleteByPrimaryKey(String deptNo);

    /**
     * 插入记录
     *
     * @param record 要插入的数据 record不能为null
     * @return 插入的记录
     */
    int insert(SysDept record);

    /**
     * 插入记录(只对record中的非null字段生成插入语句,忽略空字段)
     *
     * @param record 要插入的数据 record不能为null
     * @return 插入的记录
     */
    int insertSelective(SysDept record);

    /**
     * 根据条件查询
     *
     * @param example 查询条件,null则查询所有数据
     * @return 结果集
     */
    List<SysDept> selectByExample(SysDeptExample example);

    /**
     * 根据主键查询
     *
     * @param deptNo 主键
     * @return 结果集
     */
    SysDept selectByPrimaryKey(String deptNo);

    /**
     * 根据条件更新指定字段(只更新record中非空字段)
     *
     * @param record  要更新的数据
     * @param example 查询条件
     * @return 影响记录数
     */
    int updateByExampleSelective(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    /**
     * 根据条件更新record中所有字段
     *
     * @param record  更新来源
     * @param example 查询条件
     * @return 影响记录数
     */
    int updateByExample(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    /**
     * 根据主键更新指定字段(只更新record中非空字段)
     *
     * @param record 更新来源
     * @return 影响记录数
     */
    int updateByPrimaryKeySelective(SysDept record);

    /**
     * 根据主键更新record中所有字段
     *
     * @param record 更新来源
     * @return 影响记录数
     */
    int updateByPrimaryKey(SysDept record);

    /**
     * 根据条件和指定的字段名查询数据
     *
     * @param example 查询条件 若此条件为null则查询所有数据
     * @param fields  要得到的字段(cn.qingweico.entity.SysDept类的字段名 而不是查询返回的列名) 此方法将自动忽略匹配不到的字段 若此参数为null或length==0 则查询所有字段
     * @return 查询到的结果集
     */
    List<SysDept> selectColumnsByExample(@Param("example") SysDeptExample example, @Param("fields") String... fields);

    /**
     * 根据主键和指定的字段名查询数据
     *
     * @param deptNo 主键字段
     * @param fields 要查询的字段(cn.qingweico.entity.SysDept类的字段名 而不是数据库的列名) 此方法将自动忽略匹配不到的字段 若此参数为null或length==0 则查询除主键外所有字段
     * @return 查询到的数据
     */
    SysDept selectColumnsByPrimaryKey(@Param("deptNo") String deptNo, @Param("fields") String... fields);

    /**
     * 根据主键和指定的字段名更新非主键字段
     *
     * @param record 要更新的数据 record不能为null
     * @param fields 要更新的字段(cn.qingweico.entity.SysDept类的字段名 而不是数据库的列名) 此方法将自动忽略主键与匹配不到的字段与主键字段 若此参数为null或size==0 则更新除主键外所有字段
     * @return 受影响的行数
     */
    int updateColumnsByPrimaryKey(@Param("record") SysDept record, @Param("fields") String... fields);

    /**
     * 根据条件和指定的字段名更新字段
     *
     * @param record  要更新的数据 record不能为null
     * @param example 更新条件 若此条件为null则更新所有数据
     * @param fields  要更新的字段(cn.qingweico.entity.SysDept类的字段名 而不是数据库的列名) 此方法将自动忽略主键与匹配不到的字段 若此参数为null或size==0 则更新除主键外所有字段
     * @return 受影响的行数
     */
    int updateColumnsByExample(@Param("record") SysDept record, @Param("example") SysDeptExample example, @Param("fields") String... fields);
}
