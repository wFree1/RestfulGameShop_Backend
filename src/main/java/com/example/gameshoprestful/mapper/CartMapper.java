package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import com.example.gameshoprestful.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public interface CartMapper extends BaseMapper<Cart> {
    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    void deleteByUserId(int id);
}
