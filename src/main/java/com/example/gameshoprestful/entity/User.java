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

// 设置名字的方法
    public void setName(String name) {
        // 将传入的name参数赋值给当前对象的name属性
        this.name = name;
    }
}
