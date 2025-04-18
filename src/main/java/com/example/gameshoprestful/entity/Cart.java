package com.example.gameshoprestful.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;

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
}
