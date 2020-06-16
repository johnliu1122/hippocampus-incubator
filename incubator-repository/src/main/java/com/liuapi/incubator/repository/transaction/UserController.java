package com.liuapi.incubator.repository.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/tx/demo")
public class UserController {
    @Autowired
    private UserService2 userService2;

    @GetMapping("/addUser")
    public int addUser(){
        userService2.addUser();
        return 1;
    }
}
