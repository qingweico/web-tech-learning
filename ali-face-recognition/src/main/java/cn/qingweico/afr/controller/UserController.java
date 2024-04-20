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
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
    @Resource
    private GridFSBucket gridFsBucket;

    @PostMapping("/cu")
    public ApiResponse cu(@RequestBody SaveUser saveUser) {
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
                GridFSFindIterable gridFsFiles = gridFsBucket.find(Filters.eq("_id", new ObjectId((saveUser.getFaceId()))));
                GridFSFile gridFs = gridFsFiles.first();
                if (gridFs != null) {
                    // 删除人脸信息
                    gridFsBucket.delete(new ObjectId(saveUser.getFaceId()));
                }
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
    public ApiResponse del(@RequestBody List<String> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return ApiResponse.ok(userService.removeByIds(ids));
        }
        return ApiResponse.ok();
    }
    @PostMapping("/searchList")
    public ApiResponse searchList(@RequestBody PagedParams pagedParams) {
        PagedResult result = userService.searchList(pagedParams);
        return ApiResponse.ok(result);
    }
}
