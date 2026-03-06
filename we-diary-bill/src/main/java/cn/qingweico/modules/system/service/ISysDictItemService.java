package cn.qingweico.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.qingweico.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    List<SysDictItem> selectItemsByMainId(String mainId);
}
