package com.example.gameshoprestful.service;

import com.example.gameshoprestful.DTO.ExistingGamesDTO;
import com.example.gameshoprestful.mapper.ExistingGameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExistingGameService {
    @Autowired
    private ExistingGameMapper existingGameMapper;

    public List<ExistingGamesDTO> getItemInfosByUserId(Integer userId) {
        return existingGameMapper.findItemInfosByUserId(userId);
    }
}
