package com.checker.springdemo.controller;

import com.checker.springdemo.entity.User;
import com.checker.springdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testBoot")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("getUser/{id}")
    public String GetUserByID(@PathVariable int id){
        User user = userService.selectByID(id);
        if (user != null){
            return user.toString();
        }else{
            return "无记录";
        }
    }
}
