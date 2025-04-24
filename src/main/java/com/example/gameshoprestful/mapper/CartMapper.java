package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gameshoprestful.DTO.CartItemDTO;
import com.example.gameshoprestful.DTO.CartWithPriceDTO;
import com.example.gameshoprestful.entity.Cart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
@Component
public interface CartMapper extends BaseMapper<Cart> {

    // 查找是否已经存在该商品在购物车中
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND item_id = #{itemId} AND edition_id = #{editionId}")
    Cart findExistingCartItem(@Param("userId") int userId, @Param("itemId") int itemId, @Param("editionId") int editionId);

    // 增加商品数量或插入新商品到购物车
    @Insert("INSERT INTO cart(user_id, item_id, edition_id, price, add_count, is_selected) VALUES(#{userId}, #{itemId}, #{editionId}, #{price}, 1, 0)")
    void addNewCartItem(@Param("userId") int userId, @Param("itemId") int itemId, @Param("editionId") int editionId, @Param("price") double price);

    // 更新购物车项
    @Update("UPDATE cart SET add_count = add_count + 1, price = #{price} WHERE user_id = #{userId} AND item_id = #{itemId} AND edition_id = #{editionId}")
    void updateCartItem(@Param("userId") int userId, @Param("itemId") int itemId, @Param("editionId") int editionId, @Param("price") double price);

    // 购物车查询
    @Select("SELECT c.id AS id, u.username, i.name, i.picture1, ie.editionName, COALESCE(ie.price, i.price) AS finalPrice " +
            "FROM cart c " +
            "JOIN user u ON c.user_id = u.id " +
            "JOIN item i ON c.item_id = i.id " +
            "LEFT JOIN item_edition ie ON c.edition_id = ie.id " +
            "WHERE c.user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userName", column = "username"),
            @Result(property = "itemName", column = "name"),
            @Result(property = "picture1", column = "picture1"),
            @Result(property = "editionName", column = "editionName"),
            @Result(property = "price", column = "finalPrice")
    })
    List<CartItemDTO> findCartDetailsByUserId(Long userId);

    @Update({
            "<script>",
            "UPDATE cart SET is_selected = 1 WHERE id IN",
            "<foreach item='id' collection='cartIds' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void updateSelectionByIds(@Param("cartIds") List<Long> cartIds, boolean b);

    @Select("SELECT c.id AS cartId, c.item_id AS itemId, c.edition_id AS editionId, " +
            "i.price     AS itemPrice, " +
            "ie.price    AS editionPrice " +
            "FROM cart c " +
            "LEFT JOIN item i         ON c.item_id    = i.id " +
            "LEFT JOIN item_edition ie ON c.edition_id = ie.id " +
            "WHERE c.user_id = #{userId} AND c.is_selected = 1")
    List<CartWithPriceDTO> findSelectedCartsWithPrice(@Param("userId") Long userId);

    @Delete("DELETE FROM cart WHERE user_id = #{userId} AND is_selected = 1")
    void deleteSelectedByUserId(@Param("userId") Long userId);

    //清除购物车中对应商品
    @Delete("DELETE FROM cart WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);


    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    void deleteByUserId(int id);
}

