package cn.qingweico.common.util;

import cn.hutool.core.collection.CollectionUtil;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengbaoning
 * @version 1.0
 * @description: encache 工具类
 * @date 2021/3/31 9:39
 */

@Slf4j
@Component
public class CacheUtil {

    @Resource
    public EhCacheCacheManager manager;

    /**
     * 获取Cache类
     *
     * @param cacheName
     * @return
     */
    public Cache getCache(String cacheName) {
        return manager.getCache(cacheName);
    }

    /**
     * List添加数据
     * @author shangyue
     * @param cacheName
     * param
     */
    @SuppressWarnings("unchecked")
    public void add(String cacheName, String value) {
        Cache cache = manager.getCache(cacheName);
        List<String> list = cache.get(cacheName, ArrayList.class);
        if (CollectionUtil.isEmpty(list)) {
            list = new ArrayList<>();
        }
        list.add(value);
        cache.put(cacheName, list);

    }


    /**
     * List获取数据
     * @author shangyue
     * @param cacheName
     */
    @SuppressWarnings("unchecked")
    public List<String> get(String cacheName) {
        Cache cache = manager.getCache(cacheName);
        List<String> list = cache.get(cacheName, ArrayList.class);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list;
    }

    /**
     * List删除数据
     * @author shangyue
     * @param cacheName
     * @param value
     *
     */
    @SuppressWarnings("unchecked")
    public void remove(String cacheName, String value) {
        Cache cache = manager.getCache(cacheName);
        List<String> list = cache.get(cacheName, ArrayList.class);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        list.remove(value);
        cache.put(cacheName, list);
    }


    /**
     * 添加缓存数据
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public void put(String cacheName, String key, Object value) {
        try {
            Cache cache = manager.getCache(cacheName);
            if(null == cache){
                manager.getCacheManager().addCache(cacheName);
            }
            cache.put(key, value);
        } catch (Exception e) {
            log.error("添加缓存失败: {}", e.getMessage());
        }
    }

    /**
     * 获取缓存数据
     *
     * @param cacheName
     * @param key
     * @return
     */
    public String get(String cacheName, String key) {
        try {
            Cache cache = manager.getCache(cacheName);
            if(null == cache){
                manager.getCacheManager().addCache(cacheName);
            }

            Cache.ValueWrapper valueWrapper = cache.get(key);
            return valueWrapper == null ? null : String.valueOf(valueWrapper.get());
        } catch (Exception e) {
            log.error("获取缓存数据失败: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取缓存数据
     *
     * @param cacheName
     * @param key
     * @return
     */
    public Pointer getPointer(String cacheName, String key) {
        try {
            Cache cache = manager.getCache(cacheName);
            if (null == cache) {
                manager.getCacheManager().addCache(cacheName);
            }
            Cache.ValueWrapper valueWrapper = cache.get(key);
            return valueWrapper == null ? null : (Pointer) valueWrapper.get();
        } catch (Exception e) {
            log.error("获取缓存数据失败: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 删除缓存数据
     *
     * @param cacheName
     * @param key
     */
    public void delete(String cacheName, String key) {
        try {
            Cache cache = manager.getCache(cacheName);
            cache.evict(key);
        } catch (Exception e) {
            log.error("删除缓存数据失败: {}", e.getMessage());
        }
    }

    private class CheckEmptyUtil {
    }
}
