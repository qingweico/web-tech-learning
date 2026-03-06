package cn.qingweico.modules.television.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.modules.television.entity.TelevisionCategory;
import cn.qingweico.modules.television.mapper.TelevisionCategoryMapper;
import cn.qingweico.modules.television.service.ITelevisionCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 观影分类服务实现类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Service
public class TelevisionCategoryServiceImpl extends ServiceImpl<TelevisionCategoryMapper, TelevisionCategory> implements ITelevisionCategoryService {

    @Override
    public List<TelevisionCategory> getAllCategories(String userId) {
        LambdaQueryWrapper<TelevisionCategory> queryWrapper = new QueryWrapper<TelevisionCategory>().lambda();
        if (userId != null && !userId.isEmpty()) {
            queryWrapper.eq(TelevisionCategory::getCreatedBy, userId);
        }
        queryWrapper.orderByAsc(TelevisionCategory::getSort)
                .orderByDesc(TelevisionCategory::getCreated);
        return this.list(queryWrapper);
    }
}


