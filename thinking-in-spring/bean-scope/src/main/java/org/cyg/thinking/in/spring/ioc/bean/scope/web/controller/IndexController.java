package org.cyg.thinking.in.spring.ioc.bean.scope.web.controller;

import org.cyg.thinking.in.spring.ioc.overview.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页 Spring Web MVC 控制器
 */
@Controller
public class IndexController {
    @Autowired
    private User user; // CGLIB代理后的对象 (不变的)

    @GetMapping("/index.html")
    public String index(Model model) {
        // JSP EL 变量搜索路径 page -> request -> session -> application (ServletContext)
        // userObject -> 渲染上下文
        // user 对象存在 ServletContext ， 上下文名称: scopedTarget.user == 新生成 Bean 名称
        model.addAttribute("userObject", user);
        return "index";
    }

}
