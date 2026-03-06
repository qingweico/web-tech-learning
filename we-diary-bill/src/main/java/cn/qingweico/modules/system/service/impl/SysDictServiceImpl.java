package cn.qingweico.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qingweico.common.constant.CacheConstant;
import cn.qingweico.common.constant.CommonConstant;
import cn.qingweico.common.system.vo.DictModel;
import cn.qingweico.modules.system.entity.SysDict;
import cn.qingweico.modules.system.entity.SysDictItem;
import cn.qingweico.modules.system.mapper.SysDictItemMapper;
import cn.qingweico.modules.system.mapper.SysDictMapper;
import cn.qingweico.modules.system.model.TreeSelectModel;
import cn.qingweico.modules.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Resource
    private SysDictMapper sysDictMapper;
    @Resource
    private SysDictItemMapper sysDictItemMapper;

	/**
	 * 通过查询指定code 获取字典
	 * @param code
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstant.DICT_CACHE,key = "#code")
	public List<DictModel> queryDictItemsByCode(String code) {
		log.info("无缓存dictCache的时候调用这里！");
		return sysDictMapper.queryDictItemsByCode(code);
	}

    /**
     * 通过查询指定code 获取字典值text
     *
     * @param code
     * @param key
     * @return
     */

    @Override
    @Cacheable(value = CacheConstant.DICT_CACHE)
    public String queryDictTextByKey(String code, String key) {
        return sysDictMapper.queryDictTextByKey(code, key);
    }

    /**
     * 通过查询指定table的 text code 获取字典
     * dictTableCache采用redis缓存有效期10分钟
     *
     * @param table
     * @param text
     * @param code
     * @return
     */
    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        return sysDictMapper.queryTableDictItemsByCode(table, text, code);
    }

    @Override
    public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
        return sysDictMapper.queryTableDictItemsByCodeAndFilter(table, text, code, filterSql);
    }

    /**
     * 通过查询指定table的 text code 获取字典值text
     * dictTableCache采用redis缓存有效期10分钟
     *
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    @Override
    @Cacheable(value = "dictTableCache")
    public String queryTableDictTextByKey(String table, String text, String code, String key) {
        return sysDictMapper.queryTableDictTextByKey(table, text, code, key);
    }

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(SysDict sysDict) {
        sysDict.setDelFlag(CommonConstant.DEL_FLAG_1);
        return this.updateById(sysDict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList) {

        sysDictMapper.insert(sysDict);
        if (sysDictItemList != null) {
            for (SysDictItem entity : sysDictItemList) {
                entity.setDictId(sysDict.getId());
                sysDictItemMapper.insert(entity);
            }
        }
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return baseMapper.queryAllDepartBackDictModel();
    }

    @Override
    public List<DictModel> queryAllUserBackDictModel() {
        return baseMapper.queryAllUserBackDictModel();
    }

    @Override
    public List<DictModel> queryTableDictItems(String table, String text, String code, String keyword) {
        return baseMapper.queryTableDictItems(table, text, code, "%" + keyword + "%");
    }

    @Override
    public List<TreeSelectModel> queryTreeList(String table, String text, String code, String pidField, String pid, String hasChildField) {
        return baseMapper.queryTreeList(table, text, code, pidField, pid, hasChildField);
    }

}
