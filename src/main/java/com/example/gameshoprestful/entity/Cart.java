package com.example.gameshoprestful.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@TableName("cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "edition_id")
    private ItemEdition edition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemEdition getEdition() {
        return edition;
    }

    public void setEdition(ItemEdition edition) {
        this.edition = edition;
    }

//    /**
//     * For cart_add，以下代码负责有关购物车添加部分，与订单模块涉及到的过于差异，所以选择直接挪用
//     *
//     */
//    private int id1;
//    @TableField("user_id")
//    private int userId;
//    @TableField("item_id")
//    private int productId;
//    @TableField("edition_id")
//    private int editionId;
//    private double price;
//    private int in_cart;
//    private int add_count;
//    private int is_selected;
//
//    public int getId1() {
//        return id1;
//    }
//
//    public void setId1(int id1) {
//        this.id1 = id1;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getProductId() {
//        return productId;
//    }
//
//    public void setProductId(int productId) {
//        this.productId = productId;
//    }
//
//    public int getEditionId() {
//        return editionId;
//    }
//
//    public void setEditionId(int editionId) {
//        this.editionId = editionId;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public int getIn_cart() {
//        return in_cart;
//    }
//
//    public void setIn_cart(int in_cart) {
//        this.in_cart = in_cart;
//    }
//
//    public int getAdd_count() {
//        return add_count;
//    }
//
//    public void setAdd_count(int add_count) {
//        this.add_count = add_count;
//    }
//
//    public int getIs_selected() {
//        return is_selected;
//    }
//
//    public void setIs_selected(int is_selected) {
//        this.is_selected = is_selected;
//    }
//
////for cart_add
}
