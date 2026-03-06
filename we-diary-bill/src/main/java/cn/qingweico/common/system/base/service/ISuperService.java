package cn.qingweico.common.system.base.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISuperService<T> extends IService<T> {
    public BaseMapper getSuperMapper();
}
