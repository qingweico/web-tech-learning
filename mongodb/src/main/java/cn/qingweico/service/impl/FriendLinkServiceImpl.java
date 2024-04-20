package cn.qingweico.service.impl;

import cn.qingweico.core.impl.MongoServiceImpl;
import cn.qingweico.entity.FriendLink;
import cn.qingweico.entity.model.PostParams;
import cn.qingweico.repo.FriendLinkRepository;
import cn.qingweico.service.FriendLinkService;
import cn.qingweico.model.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 友情连接Service
 *
 * @author zqw
 * @date 2021/9/10
 * {@link MongoRepository}
 * {@link MongoTemplate}
 */
@Service
public class FriendLinkServiceImpl extends MongoServiceImpl<String, FriendLink> implements FriendLinkService {
    @Resource
    private FriendLinkRepository repo;

    @Override
    public PagedResult searchList(PostParams postParams) {
        int page = postParams.getPage();
        int pageSize = postParams.getPageSize();
        // start 0 not 1
        Pageable pageable = PageRequest.of(--page, pageSize);
        FriendLink fl = new FriendLink();
        String name = postParams.getName();
        String avl = postParams.getAvl();
        if(StringUtils.isNotEmpty(name)) {
            fl.setName(name);
        }
        if(avl != null) {
            fl.setAvl(avl);
        }
        // 条件查询

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("avl", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<FriendLink> ex = Example.of(fl, exampleMatcher);
        Page<FriendLink> friendLinkPage = repo.findAll(ex, pageable);
        PagedResult pgr = new PagedResult();
        pgr.setRows(friendLinkPage.getContent());
        pgr.setTotalNumber(friendLinkPage.getTotalElements());
        pgr.setCurrentPage(friendLinkPage.getNumber() + 1);
        pgr.setTotalPage(friendLinkPage.getTotalPages());
        return pgr;
    }

    @Override
    public void delete(String linkId) {
        repo.deleteById(linkId);
    }

    @Override
    public void deleteAll(List<String> ids) {
        for (String id : ids) {
            repo.deleteById(id);
        }
    }
}
