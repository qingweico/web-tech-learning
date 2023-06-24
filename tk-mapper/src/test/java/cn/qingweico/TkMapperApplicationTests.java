package cn.qingweico;

import cn.qingweico.entity.Goods;
import cn.qingweico.mapper.GoodsMapper;
import cn.qingweico.utils.Print;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TkMapperApplicationTests {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    DataSource dataSource;
    // -------------------- Query --------------------

    @Test
    void findOne()  {
        Goods goods = Goods.builder()
                .no("P001")
                .build();
        System.out.println(goodsMapper.selectOne(goods));
    }

    @Test
    void selectAll()  {
        System.out.println(goodsMapper.selectAll());
    }
    @Test
    void selectByExample()  {
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("unitPrice").desc();
        criteria.andEqualTo("available", 1);
        Print.toPrint(goodsMapper.selectByExample(example));
    }

    @Test
    void selectByExampleAndRowBounds()  {
        Example example = new Example(Goods.class);
        example.orderBy("inventory").desc();
        RowBounds rowBounds = new RowBounds(1, 4);
        Print.toPrint(goodsMapper.selectByExampleAndRowBounds(example, rowBounds));
    }

    @Test
    void selectByPrimaryKey()  {
        System.out.println(goodsMapper.selectByPrimaryKey("1"));
    }
    @Test
    void selectByRowBounds()  {
        Goods goods = Goods.builder()
                .available(1)
                .build();
        RowBounds rowBounds = new RowBounds(1, 2);
        Print.toPrint(goodsMapper.selectByRowBounds(goods, rowBounds));
    }
    @Test
    void selectCount()  {
        Goods goods = Goods.builder()
                .available(1)
                .build();
        Print.print(goodsMapper.selectCount(goods));
    }

    @Test
    void selectCountByExample()  {
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("available", 1);
        Print.print(goodsMapper.selectCountByExample(example));
    }
    @Test
    void selectOneByExample()  {
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("no", "P001");
        Print.print(goodsMapper.selectOneByExample(example));
    }

    // -------------------- Insert --------------------
    @Test
    void insert()  {
        Goods goods = Goods.builder()
                .name("电视")
                .unitPrice(BigDecimal.valueOf(100.0))
                .inventory(BigDecimal.valueOf(10))
                .no("P011")
                .available(1)
                .build();
        Print.print(goodsMapper.insert(goods));
    }
    @Test
    void insertList()  {
        Goods g1 = Goods.builder()
                .name("g1")
                .unitPrice(BigDecimal.valueOf(100.0))
                .inventory(BigDecimal.valueOf(10))
                .no("P012")
                .available(1)
                .build();
        Goods g2 = Goods.builder()
                .name("g2")
                .unitPrice(BigDecimal.valueOf(23.0))
                .inventory(BigDecimal.valueOf(100))
                .no("P013")
                .available(1)
                .build();
        List<Goods> goodsList = new ArrayList<>();
        goodsList.add(g1);
        goodsList.add(g2);
        Print.print(goodsMapper.insertList(goodsList));
    }
    @Test
    void insertSelective()  {
        Goods goods = Goods.builder()
                .name("g3")
                .no(null)
                .available(1)
                .build();
        Print.print(goodsMapper.insertSelective(goods));
    }
    @Test
    void insertUseGeneratedKeys()  {
        Goods goods = Goods.builder()
                .name("g4")
                .no(null)
                .available(1)
                .build();
        Print.print(goodsMapper.insertUseGeneratedKeys(goods));
    }

    // -------------------- Update --------------------

    // -------------------- Delete --------------------
}
