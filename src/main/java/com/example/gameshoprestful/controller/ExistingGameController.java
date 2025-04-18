package com.example.gameshoprestful.controller;

import com.example.gameshoprestful.DTO.ExistingGamesDTO;
import com.example.gameshoprestful.service.ExistingGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games")
public class ExistingGameController {
    @Autowired
    private ExistingGameService existingGameService;

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getItemNames(@PathVariable Integer userId) {
        List<ExistingGamesDTO> itemInfos = existingGameService.getItemInfosByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("items", itemInfos);
        return ResponseEntity.ok(response);
    }
}

