package com.example.gameshoprestful.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;

@Entity
@TableName("user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JoinColumn(name = "username")
    private String name;

    // 无参构造（JPA需要）
    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
