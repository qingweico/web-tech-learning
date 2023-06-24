package cn.qingweico.mapper;

import cn.qingweico.entity.Goods;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author zqw
 * @date 2023/6/21
 */
@Repository
public interface GoodsMapper extends Mapper<Goods>, MySqlMapper<Goods> {
}
