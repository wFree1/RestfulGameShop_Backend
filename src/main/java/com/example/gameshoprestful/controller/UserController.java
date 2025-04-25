package com.example.gameshoprestful.controller;

import com.example.gameshoprestful.DTO.UserDTO;
import jakarta.validation.Valid;
import com.example.gameshoprestful.entity.User;
import com.example.gameshoprestful.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public Map<String, Object> registerUser(@RequestParam Map<String, String> registerData) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = new User();
            user.setUsername(registerData.get("username"));
            user.setPassword(registerData.get("password"));
            user.setEmail(registerData.get("email"));

            userService.register(user);
            response.put("message", "注册成功");
            response.put("success", true);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }


    @GetMapping("")
    public Map<String, Object> login(@RequestParam Map<String, String> loginData) {
        System.out.println(loginData.toString());
        Map<String, Object> response = new HashMap<>();
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            if (username == null || password == null) {
                response.put("code", 400);
                response.put("message", "名称或密码不能为空");
                return response;
            }

            Map<String, Object> result = userService.login(username, password);

            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", result.get("user"));
            response.put("token", result.get("token"));
        } catch (IllegalArgumentException e) {
            response.put("code", 401);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "服务器错误：" + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    @PostMapping("/{id}")
    public Map<String, Object> updateUserInfo(@PathVariable int id, @Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            int result = userService.updateUserInfo(id,user);
            if (result > 0) {
                response.put("message", "更新用户信息成功");
                response.put("success", true);
            } else {
                response.put("message", "更新失败，可能是数据未发生变化");
                response.put("success", false);
            }
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getUserInfo(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
//            if (id == null || id.trim().isEmpty()) {
//                response.put("message", "缺少 id 参数");
//                response.put("success", false);
//                return response;
//            }

            int userId = id;
//            try {
//                userId = Integer.parseInt(id);
//            } catch (NumberFormatException e) {
//                response.put("message", "id 参数必须是有效的整数");
//                response.put("success", false);
//                return response;
//            }

            User user = userService.getUserInfo(userId);
            response.put("data", user);
            response.put("success", true);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }
    // UserController.java
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).build(); // 404 Not Found
        }
    }

}