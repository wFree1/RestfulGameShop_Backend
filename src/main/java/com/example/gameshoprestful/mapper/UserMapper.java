package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.gameshoprestful.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}