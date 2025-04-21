package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gameshoprestful.DTO.ExistingGamesDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExistingGameMapper extends BaseMapper<ExistingGamesDTO> {

    @Insert("INSERT IGNORE INTO existing_games(user_id, item_id, item_name) VALUES (#{userId}, #{itemId}, #{itemName})")
    void insertIgnore(@Param("userId") Integer userId,
                      @Param("itemId") Integer itemId,
                      @Param("itemName") String itemName);


    @Select("""
    SELECT i.id AS itemId,i.name AS itemName, i.picture1 
    FROM existing_games eg
    JOIN item i ON eg.item_id = i.id
    WHERE eg.user_id = #{userId}
""")
    List<ExistingGamesDTO> findItemInfosByUserId(@Param("userId") Integer userId);
}
