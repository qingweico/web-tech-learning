package cn.qingweico.core.impl;

import cn.qingweico.core.entity.Page;
import cn.qingweico.core.wrapper.LambdaQueryWrapper;
import cn.qingweico.service.MongoService;
import cn.qingweico.utils.ClassUtil;
import cn.qingweico.utils.QueryBuildUtils;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 默认的服务实现类
 *
 * @author zqw
 * @date 2023/11/04
 */
public class MongoServiceImpl<I extends Serializable, T> implements MongoService<I, T> {

    /**
     * 服务类对应的mongo实体类
     */
    @SuppressWarnings("unchecked")
    private final Class<T> targetClass = (Class<T>) ClassUtil.getGenericsInObject(this);

    @Resource
    protected MongoTemplate mongoTemplate;

    @Override
    public T getOne(LambdaQueryWrapper<T> queryWrapper) {
        if (targetClass != null) {
            Query query = QueryBuildUtils.buildQuery(queryWrapper);
            return mongoTemplate.findOne(query, targetClass);
        }
        return null;
    }

    @Override
    public boolean save(T entity) {
        mongoTemplate.save(entity);
        return true;
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {

        entityList.forEach(item -> mongoTemplate.save(item));
        return true;

    }

    @Override
    public boolean removeById(I id) {
        if (targetClass != null) {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = new Query(criteria);
            DeleteResult deleteResult = mongoTemplate.remove(query, targetClass);
            return deleteResult.getDeletedCount() > 0;
        }
        return false;

    }

    @Override
    public boolean remove(LambdaQueryWrapper<T> queryWrapper) {
        if (targetClass != null) {
            Query query = QueryBuildUtils.buildQuery(queryWrapper);
            DeleteResult remove = mongoTemplate.remove(query, targetClass);
            return remove.getDeletedCount() > 0;
        }
        return false;

    }

    @Override
    public boolean updateById(T entity) {
        if (targetClass != null) {
            Criteria criteria = Criteria.where("_id").is(ClassUtil.getId(entity));
            Query query = new Query(criteria);
            Update update = getUpdate(entity);
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, targetClass);
            return updateResult.getModifiedCount() > 0;
        }
        return false;

    }

    /**
     * 通过反射获取需要更新的字段
     */
    private Update getUpdate(T entity) {

        Update update = new Update();
        for (Field field : entity.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object result = field.get(entity);
                if (Objects.nonNull(result)) {
                    update.set(field.getName(), result);
                }
            } catch (Exception ignore) {
            }
        }
        return update;

    }

    @Override
    public boolean update(T entity, LambdaQueryWrapper<T> queryWrapper) {
        if (targetClass != null) {
            Query query = QueryBuildUtils.buildQuery(queryWrapper);
            Update update = getUpdate(entity);
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, targetClass);
            return updateResult.getModifiedCount() > 0;
        }
        return false;

    }

    @Override
    public T getById(I id) {
        if (targetClass != null) {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = new Query(criteria);
            return mongoTemplate.findOne(query, targetClass);
        }
        return null;

    }

    @Override
    public Collection<T> listByIds(Collection<I> idList) {
        if (targetClass != null) {
            Criteria criteria = Criteria.where("_id").in(idList);
            Query query = new Query(criteria);
            return mongoTemplate.find(query, targetClass);
        }
        return null;

    }

    @Override
    public long count(LambdaQueryWrapper<T> queryWrapper) {
        if (targetClass != null) {
            Query query = QueryBuildUtils.buildQuery(queryWrapper);
            return mongoTemplate.count(query, targetClass);
        }
        return 0;

    }

    @Override
    public List<T> list(LambdaQueryWrapper<T> queryWrapper) {
        if (targetClass != null) {
            Query query = QueryBuildUtils.buildQuery(queryWrapper);
            return mongoTemplate.find(query, targetClass);
        }
        return null;

    }

    @Override
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) {
        if (targetClass != null) {
            Query query = QueryBuildUtils.buildQuery(queryWrapper);
            Page<T> page = new Page<>();
            page.setPageSize(pageSize);
            page.setPageNum(pageNo);
            long total = mongoTemplate.count(query, targetClass);
            page.setTotal(total);
            if (total <= 0) {
                return page;
            }
            query.skip((long) (pageNo - 1) * pageSize).limit(pageSize);
            List<T> list = mongoTemplate.find(query, targetClass);
            page.setRecords(list);
            return page;
        }
        return null;

    }

    @Override
    public boolean exist(LambdaQueryWrapper<T> queryWrapper) {
        if (targetClass != null) {
            Query query = QueryBuildUtils.buildQuery(queryWrapper);
            return mongoTemplate.exists(query, targetClass);
        }
        return false;
    }

}
