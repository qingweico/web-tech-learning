package cn.qingweico.modules.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Danger!!!!!!!!
 * DDL操作, 一定要慎重使用,  mysql用户需要有ddl操作权限
 * 备份清空表操作
 * 调用时需要用  ${tableName}的方式传参
 */
@Mapper
@Repository
public interface BackupDataMapper {


    /**
     * truncate指定数据库表的数据
     * @param tableName
     * @return
     */
    int truncateTable(@Param("tableName") String tableName);


    /**
     * 根据传入的表明, 创建新的表并且将原表的数据插入到新的Occur表中
     * @param newTableName
     * @param originalTableName
     */
    void createNewTableAndInsertData(@Param("newTableName") String newTableName,
                                     @Param("originalTableName") String originalTableName);

    /**
     * 统计某张表中的总数据条数。
     * @param tableName
     * @return 指定表中的总记录条数。
     */
    int getRecordCount(@Param("tableName") String tableName);

    /**
     * 获得当前数据库的名字
     * @return
     */
    String getCurDataBaseName();

    /**
     * 从指定数据库中, 查询是否存在某张表
     * @param dataBaseName
     * @param tableName
     * @return
     */
    String isTargetTableExistInDB(@Param("dataBaseName") String dataBaseName,
                                  @Param("tableName") String tableName);

}
