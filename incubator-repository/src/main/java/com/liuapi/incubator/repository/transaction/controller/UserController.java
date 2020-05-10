package com.liuapi.incubator.repository.transaction.controller;

import com.liuapi.incubator.repository.transaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/create/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/requireNew")
    public int requireNew() throws Exception {
        userService.flushUserWithExceptionAndPropagationRequiredNew();
        return 1;
    }
    @GetMapping("/require")
    public int require() throws Exception{
        userService.flushUserWithExceptionAndPropagationRequired();
        return 1;
    }
    @GetMapping("/notSupport")
    public int notSupport() throws Exception{
        userService.flushUserWithExceptionAndPropagationNotSupport();
        return 1;
    }

}
