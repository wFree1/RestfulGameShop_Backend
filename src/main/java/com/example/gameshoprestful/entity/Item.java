package com.example.gameshoprestful.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@TableName("item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    // 无参构造
    public Item() {}

    //Getter Setter

}
