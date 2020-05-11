package com.liuapi.incubator.repository.transaction.controller;

import com.liuapi.incubator.repository.transaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/tx/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/require_new")
    public int requireNew() throws Exception {
        userService.addUserWithExceptionAndPropagationRequiredNew();
        return 1;
    }
    @GetMapping("/require")
    public int require() throws Exception{
        userService.addUserWithExceptionAndPropagationRequired();
        return 1;
    }
    @GetMapping("/not_support")
    public int notSupport() throws Exception{
        userService.addUserWithExceptionAndPropagationNotSupport();
        return 1;
    }
    @GetMapping("/support")
    public int support() throws Exception{
        userService.addUserWithExceptionAndPropagationSupport();
        return 1;
    }
    @GetMapping("/nested")
    public int nested(){
        userService.addUserWithNestedLog();
        return 1;
    }

    @GetMapping("/mandatory")
    public int mandatory(){
        userService.addUserMustNeedExistingTransactionOrThrowException();
        return 1;
    }

    @GetMapping("/never")
    public int never(){
        userService.addUserNeverUseTransactionOrThrowException();
        return 1;
    }

}
