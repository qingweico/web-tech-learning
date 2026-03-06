package cn.qingweico.common.system.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.common.system.base.entity.BaseEntity;
import cn.qingweico.common.system.base.entity.SuperEntity;
import cn.qingweico.common.system.base.mapper.JDDMapper;
import cn.qingweico.common.system.base.service.ISuperService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 通用公工service
 */
@Service
public class SuperServiceImpl<M extends JDDMapper<T>, T extends SuperEntity> extends ServiceImpl<M,T> implements ISuperService<T> {


    private void beforeSave(T entity){
        if (BaseEntity.class.isAssignableFrom(entity.getClass())) {
            BaseEntity baseEntity = (BaseEntity) entity;
            if (baseEntity.getCreated() == null) {
                baseEntity.setCreated(new Date());
            }
            if (baseEntity.getLastUpd() == null) {
                baseEntity.setLastUpd(baseEntity.getCreated());
            }
        }
    }


    @Override
    public boolean save(T entity) {
        beforeSave(entity);
        return super.save(entity);
    }


    @Override
    public boolean updateById(T entity) {
        if (BaseEntity.class.isAssignableFrom(entity.getClass())) {
            BaseEntity baseEntity = (BaseEntity) entity;
            if (baseEntity.getLastUpd() == null) {
                baseEntity.setLastUpd(new Date());
            }
        }
        return super.updateById(entity);
    }


    @Override
    public BaseMapper<T> getSuperMapper(){
        return baseMapper;
    }
}
