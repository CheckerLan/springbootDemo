package com.checker.springdemo.mapper;

import com.checker.springdemo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    User selectByID(int id);
}
