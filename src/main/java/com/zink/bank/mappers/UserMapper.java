package com.zink.bank.mappers;

import com.zink.bank.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    Integer selectUser(Integer userId);

    Integer createUser(User user);

    void updateUser(User user);

}