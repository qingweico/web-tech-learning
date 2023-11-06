package cn.qingweico.controller;

import cn.hutool.core.date.DateUtil;
import cn.qingweico.core.entity.Page;
import cn.qingweico.core.wrapper.LambdaQueryWrapper;
import cn.qingweico.core.wrapper.Wrappers;
import cn.qingweico.entity.Book;
import cn.qingweico.entity.model.BookModel;
import cn.qingweico.service.BookService;
import cn.qingweico.utils.ApiResponse;
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
    public ApiResponse<Object> save(@RequestBody BookModel um) {
        Book user = new Book();
        BeanUtils.copyProperties(um, user);
        user.setCreateTime(DateUtil.now());
        user.setUpdateTime(DateUtil.now());
        return ApiResponse.builder()
                .data(userService.save(user))
                .build();
    }

    @PostMapping("/update")
    public ApiResponse<Object> update(@RequestBody BookModel um) {
        Book user = new Book();
        BeanUtils.copyProperties(um, user);
        user.setUpdateTime(DateUtil.now());
        return ApiResponse.builder()
                .data(userService.updateById(user))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Object> deleteById(@PathVariable String id) {
        return ApiResponse.builder()
                .data(userService.removeById(id))
                .build();
    }

    @GetMapping("/get/{id}")
    public ApiResponse<Object> getById(@PathVariable String id) {
        return ApiResponse.builder()
                .data(userService.getById(id))
                .build();
    }

    @PostMapping("/page")
    public ApiResponse<Object> queryList(@RequestBody BookModel um) {
        LambdaQueryWrapper<Book> query = Wrappers.<Book>lambdaQuery()
                .eq(Objects.nonNull(um.getName()), Book::getName, um.getName());
        Page<Book> page = userService.page(query, um.getPageNo(), um.getPageSize());
        return ApiResponse.builder()
                .data(page)
                .build();
    }
}
