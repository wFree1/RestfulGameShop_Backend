package com.example.gameshoprestful.DTO;

import java.math.BigDecimal;

public class CartItemDTO {
    private Long id;
    private String userName;
    private String itemName;
    private String picture1;
    private String editionName;
    private BigDecimal price;

    // 无参构造（JSON序列化需要）
    public CartItemDTO() {}

    // 全参构造
    public CartItemDTO(String userName, String itemName, String editionName, BigDecimal price) {
        this.userName = userName;
        this.itemName = itemName;
        this.editionName = editionName;
        this.price = price;
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getEditionName() { return editionName; }
    public void setEditionName(String editionName) { this.editionName = editionName; }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}