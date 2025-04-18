package com.example.gameshoprestful.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartWithPriceDTO {
    private Long cartId;
    private Long itemId;
    private Long editionId;       // 可能为 NULL
    private BigDecimal itemPrice;
    private BigDecimal editionPrice; // 可能为 NULL

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getEditionId() {
        return editionId;
    }

    public void setEditionId(Long editionId) {
        this.editionId = editionId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getEditionPrice() {
        return editionPrice;
    }

    public void setEditionPrice(BigDecimal editionPrice) {
        this.editionPrice = editionPrice;
    }
}