package cn.qingweico.modules.bill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.modules.bill.entity.BillCategory;

import java.util.List;

/**
 * 账单分类服务接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface IBillCategoryService extends IService<BillCategory> {

    /**
     * 根据类型查询分类列表
     *
     * @param categoryType 分类类型：1-收入，2-支出
     * @param userId       用户ID（可选）
     * @return 分类列表
     */
    List<BillCategory> getCategoriesByType(Integer categoryType, String userId);
}


