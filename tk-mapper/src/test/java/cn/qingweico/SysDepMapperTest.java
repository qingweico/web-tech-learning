package cn.qingweico;

import cn.qingweico.entity.SysDept;
import cn.qingweico.io.Print;
import cn.qingweico.mapper.SysDeptMapper;
import cn.qingweico.repository.SysDeptExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author zqw
 * @date 2025/7/18
 */
@Slf4j
@SpringBootTest
public class SysDepMapperTest {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Test
    public void countByExample() {
        SysDeptExample example = new SysDeptExample();
        SysDeptExample.Criteria criteria = example.createCriteria();
        criteria.andDeptNameEqualTo("行政部");
        long result = sysDeptMapper.countByExample(example);
        log.info(">>>>>>>>>>>> count by condition: {} ", result);
        result = sysDeptMapper.countByExample(null);
        log.info(">>>>>>>>>>>> count all: {} ", result);
    }
    @Test
    public void deleteByPrimaryKey() {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey("D008");
        log.info("[D008]: {}", sysDept);
        sysDeptMapper.deleteByPrimaryKey("D008");
        sysDept = sysDeptMapper.selectByPrimaryKey("D008");
        log.info("[D008]: {}", sysDept);
    }
    @Test
    public void updateByExample() {
        log.info("[D002]: {}", sysDeptMapper.selectByPrimaryKey("D002"));
        SysDept sysDept = new SysDept();
        sysDept.setDeptName("<UNK>2");
        sysDept.setDeptNo("999");
        sysDept.setParentDeptNo(null);
        sysDept.setState("1");
        sysDept.setRemark("更新测试测试");
        SysDeptExample sysDeptExample = new SysDeptExample();
        SysDeptExample.Criteria criteria = sysDeptExample.createCriteria();
        criteria.andDeptNoEqualTo("D002");
        sysDeptMapper.updateByExample(sysDept, sysDeptExample);
        log.info("[D002]: {}", sysDeptMapper.selectByPrimaryKey("999"));
    }

    @Test
    public void selectColumnsByExample() {
        SysDeptExample sysDeptExample = new SysDeptExample();
        SysDeptExample.Criteria criteria = sysDeptExample.createCriteria();
        criteria.andParentDeptNoIsNull();
        List<SysDept> deptName = sysDeptMapper.selectColumnsByExample(sysDeptExample, "deptName");
        Print.printColl(deptName);
    }

    @Test
    public void insert() {
        SysDept sysDept = new SysDept();
        sysDept.setDeptName("测试插入部门");
        sysDept.setDeptNo("557185");
        sysDeptMapper.insert(sysDept);
    }

    @Test
    public void updateByPrimaryKey() {
        SysDept sysDept = new SysDept();
        sysDept.setDeptNo("557185");
        sysDeptMapper.updateByPrimaryKey(sysDept);
    }
    @Test
    public void updateByExampleSelective() {
        SysDept sysDept = new SysDept();
        sysDept.setRemark("这个是备注");
        SysDeptExample sysDeptExample = new SysDeptExample();
        SysDeptExample.Criteria criteria = sysDeptExample.createCriteria();
        criteria.andDeptNoEqualTo("557185");
        sysDeptMapper.updateByExampleSelective(sysDept, sysDeptExample);
    }
}
