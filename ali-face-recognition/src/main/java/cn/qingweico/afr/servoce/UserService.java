package cn.qingweico.afr.servoce;

import cn.qingweico.afr.entity.User;
import cn.qingweico.afr.model.PagedParams;
import cn.qingweico.model.PagedResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * @author zqw
 * @date 2024/1/29
 */
public interface UserService extends IService<User> {

    User findOneByUsername(String username);

    PagedResult searchList(PagedParams pagedParams);

    boolean delete(Set<String> ids);
}
