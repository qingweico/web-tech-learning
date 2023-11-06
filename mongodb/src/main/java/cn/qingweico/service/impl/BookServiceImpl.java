package cn.qingweico.service.impl;

import cn.qingweico.core.impl.MongoServiceImpl;
import cn.qingweico.entity.Book;
import cn.qingweico.service.BookService;
import org.springframework.stereotype.Service;

/**
 * @author zqw
 * @date 2023/11/5
 */
@Service
public class BookServiceImpl extends MongoServiceImpl<String, Book> implements BookService {
}
