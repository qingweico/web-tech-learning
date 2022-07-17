package cn.qingweico.bean.scope.web.config;

import cn.qingweico.ioc.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author zqw
 * @date 2021/11/21
 */
@Configuration
@EnableWebMvc
public class WebMvcConfiguration {

    @Bean
    @RequestScope
    public User user () {
        User user = new User();
        user.setId(1L);
        user.setName("li");
        return user;
    }
}
