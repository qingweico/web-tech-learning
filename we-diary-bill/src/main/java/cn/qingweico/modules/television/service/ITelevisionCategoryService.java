package cn.qingweico.modules.television.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.modules.television.entity.TelevisionCategory;

import java.util.List;

/**
 * 观影分类服务接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface ITelevisionCategoryService extends IService<TelevisionCategory> {

    /**
     * 获取所有分类列表（按排序）
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 分类列表
     */
    List<TelevisionCategory> getAllCategories(String userId);
}







