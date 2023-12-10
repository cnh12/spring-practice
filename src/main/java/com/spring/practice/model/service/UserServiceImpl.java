package com.spring.practice.model.service;

import com.spring.practice.model.User;
import com.spring.practice.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper mapper;
    @Override
    public List<User> getUserList() {
        return mapper.getUserList();
    }

}
