package cn.qingweico.modules.bill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.modules.bill.entity.BillCategory;
import cn.qingweico.modules.bill.mapper.BillCategoryMapper;
import cn.qingweico.modules.bill.service.IBillCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账单分类服务实现类
 *
 * @author zqw
 * @date 2025/01/XX
 */
@Slf4j
@Service
public class BillCategoryServiceImpl extends ServiceImpl<BillCategoryMapper, BillCategory> implements IBillCategoryService {

    @Resource
    private BillCategoryMapper billCategoryMapper;

    @Override
    public List<BillCategory> getCategoriesByType(Integer categoryType, String userId) {
        return billCategoryMapper.selectByType(categoryType, userId);
    }
}


