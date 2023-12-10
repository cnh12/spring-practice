package com.spring.practice.controller;


import com.spring.practice.model.User;
import com.spring.practice.model.mapper.UserMapper;
import com.spring.practice.model.service.FcmService;
import com.spring.practice.model.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    FcmService fcmService;

    @GetMapping("/userList")
    public List<User> getUserList(){
        log.info("getUserList 정상 수신");
        List<User> lst = service.getUserList();
        return lst;
    }


    @GetMapping("/testList")
    public List<String> testList(){
        log.info("testList 정상 수신");
        List<String> lst = new ArrayList<>();
        lst.add("test1");
        lst.add("test2");
        lst.add("test3");
        lst.add("test4");
        lst.add("test5");
        lst.add("test6");
        lst.add("test7");
        return lst;
    }

    @PostMapping("/sendFcm")
    public void sendFcm(){
        log.info("sendFcm 정상 수신3");

        fcmService.sendFcm();
    }


    /*
    --------------------------테스트 ---------------
     */



}
