package cn.qingweico;

import cn.qingweico.io.Print;
import cn.qingweico.jpa.JpaApplication;
import cn.qingweico.jpa.entity.Role;
import cn.qingweico.jpa.repository.RoleRepository;
import cn.qingweico.supplier.RandomDataGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zqw
 * @date 2026/1/6
 */
@SpringBootTest(classes = JpaApplication.class)
public class JpaApplicationTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void insert() {
        Role role = new Role();
        role.setName(RandomDataGenerator.name(true));
        role.setCode(RandomStringUtils.randomAlphanumeric(6));
        role.setDescription(RandomStringUtils.random(20, true, true));
        roleRepository.saveAndFlush(role);
    }

    @Test
    public void findAll() {
        Print.printColl(roleRepository.findAll());
    }
}
