package cn.qingweico;

import cn.qingweico.entity.Goods;
import cn.qingweico.mapper.GoodsMapper;
import cn.qingweico.utils.Print;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private GoodsMapper goodsMapper;

    @Test
    void selectAll() {
        Print.toPrint(goodsMapper.selectList(null));
    }

    @Test
    void selectPartCol() {
        // 查询部分字段
        LambdaQueryWrapper<Goods> wq = new LambdaQueryWrapper<>();
        wq.select(Goods::getName, Goods::getInventory, Goods::getUnitPrice);
        Print.toPrint(goodsMapper.selectList(wq));
    }
    @Test
    void subQuery() {
        // 子查询
        LambdaQueryWrapper<Goods> wq = new LambdaQueryWrapper<>();
        wq.inSql(Goods::getUnitPrice, "select unit_price from tb_goods where unit_price >= 10.0");
        Print.toPrint(goodsMapper.selectList(wq));
    }

    @Test
    void paged() {
        // offset当前行数 : offset = (currenPage - 1) * rows
        // currenPage从1开始 offset初始行的偏移量为0
        // rows 返回的行数
        LambdaQueryWrapper<Goods> wq = new LambdaQueryWrapper<>();
        wq.orderByDesc(Goods::getInventory);
        Page<Goods> page = new Page<>(0 ,3);
        goodsMapper.selectPage(page, wq);
        Print.toPrint(page.getRecords());
    }

}
