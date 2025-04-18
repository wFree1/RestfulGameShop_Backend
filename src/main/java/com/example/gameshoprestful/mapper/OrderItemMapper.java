package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gameshoprestful.DTO.OrderItemDTO;
import com.example.gameshoprestful.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface OrderItemMapper extends BaseMapper<OrderItem> {

    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItem> selectByOrderId(@Param("orderId") Integer orderId);


    @Select("""
    SELECT 
        oi.id,
        oi.item_id,
        i.name AS item_name,
        ie.editionName AS editionName,
        oi.price,
        i.picture1
    FROM order_items oi
    JOIN item i ON oi.item_id = i.id
    JOIN item_edition ie ON oi.edition_id = ie.id
    WHERE oi.order_id = #{orderId}
""")
    List<OrderItemDTO> findByOrderId(int orderId);

}
