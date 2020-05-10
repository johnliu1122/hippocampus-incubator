package com.liuapi.incubator.repository.transaction.mapper;

import com.liuapi.incubator.repository.transaction.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    List<User> getAllUser();

    User load(@Param("id")Long id);

    Integer addUser(User user);

    Integer updateUserById(User user);

    Integer deleteUserById(@Param("id") Long id);
}