package com.checker.springdemo.service;

import com.checker.springdemo.entity.User;
import com.checker.springdemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User selectByID(int id){
        return userMapper.selectByID(id);
    }
}
