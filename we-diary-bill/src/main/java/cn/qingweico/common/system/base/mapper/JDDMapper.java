package cn.qingweico.common.system.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qingweico.common.system.base.entity.SuperEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jdd
 */
@Mapper
public interface JDDMapper<T extends SuperEntity> extends BaseMapper<T> {
}
