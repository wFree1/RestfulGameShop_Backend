package com.example.gameshoprestful.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.gameshoprestful.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository()
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
