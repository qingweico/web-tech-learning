package cn.qingweico.mapper;

import cn.qingweico.entity.Locker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author zqw
 * @date 2025/8/2
 */
@Mapper
public interface LockerMapper {

    int insert(Locker record);

    int increaseByCondition(@Param("lockName") String lockName, @Param("lockKey") String lockKey,@Param("limitTime") Date limitTime);

    int reduceByCondition(@Param("lockName") String lockName, @Param("lockKey") String lockKey);

    int deleteByCondition(@Param("lockName") String lockName, @Param("lockKey") String lockKey, @Param("limitTime") Date limitTime, @Param("lockCount") int lockCount);

    int deleteOutOfDate(@Param("lockName") String lockName, @Param("limitTime") Date limitTime);
}
