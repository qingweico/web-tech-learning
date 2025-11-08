package cn.qingweico.afr.servoce.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qingweico.afr.entity.User;
import cn.qingweico.afr.mapper.UserMapper;
import cn.qingweico.afr.model.PagedParams;
import cn.qingweico.afr.servoce.UserService;
import cn.qingweico.constants.Symbol;
import cn.qingweico.model.PagedResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author zqw
 * @date 2024/1/29
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private GridFSBucket gridFsBucket;
    @Resource
    private ExecutorService pool;

    @Override
    public User findOneByUsername(String username) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername, username);
        return userMapper.selectOne(lqw);
    }

    @Override
    public PagedResult searchList(PagedParams pagedParams) {
        int pageNum = pagedParams.getPage();
        int pageSize = pagedParams.getPageSize();
        LambdaQueryWrapper<User> wq = new LambdaQueryWrapper<>();
        wq.orderByDesc(User::getCreateTime);
        Page<User> page = new Page<>(pageNum, pageSize);
        IPage<User> selectedPage = userMapper.selectPage(page, wq);
        PagedResult pr = new PagedResult();
        pr.setRows(selectedPage.getRecords());
        pr.setCurrentPage(selectedPage.getCurrent());
        pr.setTotalNumber(selectedPage.getTotal());
        pr.setTotalPage(selectedPage.getPages());
        return pr;
    }

    @Override
    public boolean delete(Set<String> ids) {
        // 如果有人脸信息需要先删除GridFS中的人脸信息
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().in(User::getId, ids);
        List<User> users = userMapper.selectList(wrapper);
        boolean remove = this.removeByIds(ids);
        if (remove && CollUtil.isNotEmpty(users)) {
            pool.execute(() -> {
                List<String> faces = users.stream()
                        .map(User::getFaceId)
                        .filter(StringUtils::isNotEmpty)
                        .toList();
                log.info("删除的用户 GridFS 中存在人脸信息, 需要删除人脸信息, {}", StringUtils.join(faces, Symbol.COMMA));
                if (CollUtil.isNotEmpty(faces)) {
                    faces.forEach(this::deleteGridFsById);
                }
            });
        }
        return remove;
    }

    public void deleteGridFsById(String faceId) {
        GridFSFindIterable gridFsFiles = gridFsBucket.find(Filters.eq("_id", new ObjectId(faceId)));
        GridFSFile gridFs = gridFsFiles.first();
        if (gridFs != null) {
            // 删除人脸信息
            gridFsBucket.delete(new ObjectId(faceId));
        }
    }
}
