package cn.qingweico.modules.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qingweico.modules.bill.entity.BillCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账单分类 Mapper 接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface BillCategoryMapper extends BaseMapper<BillCategory> {

    /**
     * 根据类型查询分类列表
     *
     * @param categoryType 分类类型：1-收入，2-支出
     * @param userId       用户ID（可选，用于查询用户自定义分类）
     * @return 分类列表
     */
    List<BillCategory> selectByType(@Param("categoryType") Integer categoryType, @Param("userId") String userId);
}


