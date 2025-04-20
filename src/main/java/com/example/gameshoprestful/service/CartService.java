package com.example.gameshoprestful.service;

import com.example.gameshoprestful.DTO.CartItemDTO;
import com.example.gameshoprestful.DTO.CartDTO;
import com.example.gameshoprestful.entity.Cart;
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
    //添加到购物车
    public void addToCart(int userId, int itemId, int editionId, double price) {
        QueryWrapper<Cart> qw = new QueryWrapper<>();
        qw.eq("user_id", userId)
                .eq("item_id", itemId)
                .eq("edition_id", editionId);

        Cart existing = cartMapper.selectOne(qw);
        if (existing != null) {
            existing.setIn_cart(1);
            existing.setAdd_count(existing.getAdd_count() + 1);
            existing.setPrice(price);
            cartMapper.updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(itemId);
            cart.setEditionId(editionId);
            cart.setPrice(price);
            cart.setIn_cart(1);
            cart.setAdd_count(1);
            cart.setIs_selected(1);
            cartMapper.insert(cart);
        }
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
