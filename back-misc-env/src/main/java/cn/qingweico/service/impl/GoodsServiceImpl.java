package cn.qingweico.service.impl;

import cn.qingweico.entity.Goods;
import cn.qingweico.mapper.GoodsMapper;
import cn.qingweico.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author zqw
 * @date 2023/6/22
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
}
