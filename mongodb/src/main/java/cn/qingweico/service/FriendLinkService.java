package cn.qingweico.service;

import cn.qingweico.entity.model.PostParams;
import cn.qingweico.model.PagedResult;

import java.util.List;

/**
 * @author zqw
 * @date 2021/9/10
 */
public interface FriendLinkService {
    /**
     * 查询所有的友情链接
     * @param postParams PostParams
     * @return PagedResult
     */
    PagedResult searchList(PostParams postParams);

    /**
     * 根据友情链接id删除友情链接
     *
     * @param linkId 友情链接id
     */
    void delete(String linkId);

    /**
     * 批量删除友情链接
     *
     * @param ids 友情链接id集合
     */
    void deleteAll(List<String> ids);
}
