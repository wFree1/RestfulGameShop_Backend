package com.example.gameshoprestful.mapper;

import com.example.gameshoprestful.DTO.ExistingGamesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExistingGameMapper {
    @Select("""
    SELECT i.name AS item_name, i.picture1 
    FROM existing_games eg
    JOIN item i ON eg.item_id = i.id
    WHERE eg.user_id = #{userId}
""")
    List<ExistingGamesDTO> findItemInfosByUserId(@Param("userId") Integer userId);
}
