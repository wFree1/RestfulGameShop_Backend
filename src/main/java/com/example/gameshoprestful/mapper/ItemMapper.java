package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gameshoprestful.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {
    @Select("SELECT name FROM item WHERE id = #{itemId}")
    String selectItemNameById(@Param("itemId") Integer itemId);

}
