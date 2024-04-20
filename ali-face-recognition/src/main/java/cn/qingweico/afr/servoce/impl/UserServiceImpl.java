package cn.qingweico.afr.servoce.impl;

import cn.qingweico.afr.entity.User;
import cn.qingweico.afr.mapper.UserMapper;
import cn.qingweico.afr.model.PagedParams;
import cn.qingweico.afr.servoce.UserService;
import cn.qingweico.model.PagedResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2024/1/29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
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
        Page<User> page = new Page<>(pageNum ,pageSize);
        IPage<User> selectedPage = userMapper.selectPage(page, wq);
        PagedResult pr = new PagedResult();
        pr.setRows(selectedPage.getRecords());
        pr.setCurrentPage(selectedPage.getCurrent());
        pr.setTotalNumber(selectedPage.getTotal());
        pr.setTotalPage(selectedPage.getPages());
        return pr;
    }
}
