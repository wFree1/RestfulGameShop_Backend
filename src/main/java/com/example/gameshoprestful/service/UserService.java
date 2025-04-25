package com.example.gameshoprestful.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.gameshoprestful.entity.User;
import com.example.gameshoprestful.DTO.UserDTO;
import com.example.gameshoprestful.mapper.UserMapper;
import com.example.gameshoprestful.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean matches(String password, String storedPassword) {
        return password.equals(storedPassword); // 直接比较明文密码
    }
    public Map<String, Object> login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (!matches(password, user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }

        String token = jwtUtil.generateToken(Math.toIntExact(user.getId()));
        Map<String, Object> result = new HashMap<>();
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        result.put("user", userDTO); // 使用 DTO 避免返回密码
        result.put("token", token);
        return result;
    }

    public void register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        // 设置默认密码
        if (user.getPassword() == null) {
            user.setPassword("123456"); // 明文默认密码
            // 或使用加密密码
            // user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        }
        userMapper.insert(user);
    }

    public User getUserInfo(int id) {
        User user= userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return user;
    }

    public  int updateUserInfo(int id,User user) {
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("商家不存在");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (existingUser != null && existingUser.getId() != id) {
            throw new IllegalArgumentException("用户名已存在");
        }

        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(user.getPassword());
        }

        userMapper.updateById(user);
        return userMapper.updateById(user);
    }



    // 新增方法：生成并存储验证码到 Redis
    public byte[] generateCaptcha(String username) throws IOException {
        int width = 160, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        Random random = new Random();
        String captchaStr = "";
        for (int i = 0; i < 5; i++) {
            char ch = (char) (random.nextInt(26) + 65); // 生成大写字母
            captchaStr += ch;
        }

        // 存储到 Redis，5 分钟过期
        redisTemplate.opsForValue().set("captcha:" + username, captchaStr, 5, TimeUnit.MINUTES);

        // 绘制验证码图片
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.BLACK);
        g.drawString(captchaStr, 20, 30);
        g.dispose();

        // 将图片转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }

    // UserService.java
    public void deleteUser(int id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        userMapper.deleteById(id);
    }
}
//class UserDTO {
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    private int id;
//    private String username;
//    private String email;
//
//    public UserDTO(int id, String username, String email) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//    }
//
//}