package cn.qingweico.modules.television.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.modules.television.entity.TelevisionTag;
import cn.qingweico.modules.television.mapper.TelevisionTagMapper;
import cn.qingweico.modules.television.service.ITelevisionTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 观影标签服务实现类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Service
public class TelevisionTagServiceImpl extends ServiceImpl<TelevisionTagMapper, TelevisionTag> implements ITelevisionTagService {

    @Override
    public List<TelevisionTag> getAllTags(String userId) {
        LambdaQueryWrapper<TelevisionTag> queryWrapper = new QueryWrapper<TelevisionTag>().lambda();
        if (userId != null && !userId.isEmpty()) {
            queryWrapper.eq(TelevisionTag::getCreatedBy, userId);
        }
        queryWrapper.orderByAsc(TelevisionTag::getSort)
                .orderByDesc(TelevisionTag::getCreated);
        return this.list(queryWrapper);
    }
}


