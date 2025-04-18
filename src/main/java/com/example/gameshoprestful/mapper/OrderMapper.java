package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gameshoprestful.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 取 orders 表当前最大的 id
     */
    @Select("SELECT MAX(id) FROM orders")
    Integer selectMaxId();

    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    List<Order> findByUserId(@Param("userId") Integer userId);


    @Update("UPDATE orders SET status = #{status} WHERE id = #{orderId}")
    void updateStatus(@Param("orderId") Integer orderId,
                      @Param("status")   Integer status);
}
