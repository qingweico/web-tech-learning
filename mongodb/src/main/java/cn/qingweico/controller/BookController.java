package cn.qingweico.controller;

import cn.hutool.core.date.DateUtil;
import cn.qingweico.core.entity.Page;
import cn.qingweico.core.wrapper.LambdaQueryWrapper;
import cn.qingweico.core.wrapper.Wrappers;
import cn.qingweico.entity.Book;
import cn.qingweico.entity.model.BookModel;
import cn.qingweico.service.BookService;
import cn.qinwweico.model.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author zqw
 * @date 2023/11/5
 */
@RestController
@RequestMapping("/mongo/user")
public class BookController {

    @Resource
    private BookService userService;

    @PostMapping("/save")
    public ApiResponse save(@RequestBody BookModel um) {
        Book user = new Book();
        BeanUtils.copyProperties(um, user);
        user.setCreateTime(DateUtil.now());
        user.setUpdateTime(DateUtil.now());
        return ApiResponse.ok(userService.save(user));
    }

    @PostMapping("/update")
    public ApiResponse update(@RequestBody BookModel um) {
        Book user = new Book();
        BeanUtils.copyProperties(um, user);
        user.setUpdateTime(DateUtil.now());
        return ApiResponse.ok(userService.updateById(user));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteById(@PathVariable String id) {
        return ApiResponse.ok(userService.removeById(id));
    }

    @GetMapping("/get/{id}")
    public ApiResponse getById(@PathVariable String id) {
        return ApiResponse.ok(userService.getById(id));
    }

    @PostMapping("/page")
    public ApiResponse queryList(@RequestBody BookModel um) {
        LambdaQueryWrapper<Book> query = Wrappers.<Book>lambdaQuery().eq(Objects.nonNull(um.getName()), Book::getName, um.getName());
        Page<Book> page = userService.page(query, um.getPageNo(), um.getPageSize());
        return ApiResponse.ok(page);
    }
}
