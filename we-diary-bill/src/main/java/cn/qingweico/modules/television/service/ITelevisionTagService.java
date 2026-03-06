package cn.qingweico.modules.television.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.modules.television.entity.TelevisionTag;

import java.util.List;

/**
 * 观影标签服务接口
 *
 * @author zqw
 * @date 2025/01/XX
 */
public interface ITelevisionTagService extends IService<TelevisionTag> {

    /**
     * 获取所有标签列表（按排序）
     *
     * @param userId 用户ID（null表示所有用户）
     * @return 标签列表
     */
    List<TelevisionTag> getAllTags(String userId);
}







