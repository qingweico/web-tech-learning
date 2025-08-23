package cn.qingweico.afr.controller;

import cn.hutool.core.collection.CollUtil;
import cn.qingweico.afr.entity.User;
import cn.qingweico.afr.model.PagedParams;
import cn.qingweico.afr.model.SaveUser;
import cn.qingweico.afr.servoce.impl.UserServiceImpl;
import cn.qingweico.model.ApiResponse;
import cn.qingweico.model.PagedResult;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

/**
 * @author zqw
 * @date 2024/1/25
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserServiceImpl userService;


    @PostMapping("/cu")
    public ApiResponse<?> cu(@RequestBody SaveUser saveUser) {
        if (StringUtils.isEmpty(saveUser.getUsername())) {
            return ApiResponse.error("用户名为空");
        }
        if (saveUser.getEnableFace() && StringUtils.isEmpty(saveUser.getFaceId())) {
            return ApiResponse.error("人脸信息不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(saveUser, user);
        if (StringUtils.isNotEmpty(saveUser.getId())) {
            LambdaUpdateWrapper<User> wrapper = Wrappers.<User>lambdaUpdate()
                    .eq(User::getId, user.getId());
            if (!saveUser.getEnableFace() && StringUtils.isNotEmpty(saveUser.getFaceId())) {
                userService.deleteGridFsById(saveUser.getFaceId());
                wrapper.set(User::getFaceId, null);
            }
            user.setUpdateTime(LocalDateTime.now());
            userService.update(user, wrapper);
            return ApiResponse.ok();
        } else {
            User u = userService.findOneByUsername(saveUser.getUsername());
            if (u != null) {
                return ApiResponse.error("用户已存在");
            }
            user.setUpdateTime(LocalDateTime.now());
            user.setCreateTime(LocalDateTime.now());
            return ApiResponse.ok(userService.save(user));
        }
    }
    @PostMapping("/del")
    public ApiResponse<?> del(@RequestBody List<String> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return ApiResponse.ok(userService.delete(new HashSet<>(ids)));
        }
        return ApiResponse.ok();
    }
    @PostMapping("/searchList")
    public ApiResponse<PagedResult> searchList(@RequestBody PagedParams pagedParams) {
        PagedResult result = userService.searchList(pagedParams);
        return ApiResponse.ok(result);
    }
}
