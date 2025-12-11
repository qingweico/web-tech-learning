package cn.qingweico.controller;

import cn.qingweico.database.SqlTmplQuery;
import cn.qingweico.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @see AopProxyUtils#getSingletonTarget 获取代理对象
 * @see AopContext#currentProxy() 获取原始目标对象(可以理解与getSingletonTarget互为逆操作的关系)
 * @author zqw
 * @date 2025/11/7
 */
@Slf4j
@RestController
public class NestedTxRollbackController {

    @Resource
    SqlTmplQuery sqlTmplQuery;

    @GetMapping("/tx")
    public ApiResponse<?> tx() {
        ((NestedTxRollbackController) AopContext.currentProxy()).service();
        // 事务不会回滚
        // service();
        return ApiResponse.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public void service() {
        sqlTmplQuery.update("INSERT INTO `tb_goods` " +
                "VALUES (11, 'Product 11', 'P0011', 10.50, 100.00, 'Test remark 11', '1', NULL);");
        throw new RuntimeException();
    }
}
