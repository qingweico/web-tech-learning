package cn.qingweico.bean.scope.web.controller;

import cn.qingweico.ioc.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2021/11/21
 */
@Controller
public class IndexController {

    // CGLIB 代理对象(不变的)
    @Resource
    private User user;

    @GetMapping("index")
    public String index(Model model) {
        model.addAttribute("user", user);
        return "index";
    }
}
