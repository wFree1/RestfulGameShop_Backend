package com.example.gameshoprestful.service;

import com.example.gameshoprestful.DTO.CartItemDTO;
import com.example.gameshoprestful.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartService {

    private CartMapper cartMapper;
    @Autowired
    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    // 获取购物车详情
    public List<CartItemDTO> getCartDetails(Long userId) {
        // 直接调用 CartMapper 查询，返回的是 List<CartItemDTO>
        return cartMapper.findCartDetailsByUserId(userId);
    }

    //勾选购物车中商品
    public void updateCartSelection(List<Long> cartIds) {
        cartMapper.updateSelectionByIds(cartIds, true);
    }

    //删除购物车中对应商品
    public boolean deleteCartById(Integer id) {
        return cartMapper.deleteById(id) > 0;
    }
}
