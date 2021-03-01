package org.wdzl.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wdzl.entity.User;

@Controller
public class testController {

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(){
        return "test";
    }

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }


    @RequestMapping(value = "/userlist")
    public String getUserList(Model model){
        User user = new User();
        user.setUsername("用户名");
        model.addAttribute("user",user);
        return "userlist";
    }
}
