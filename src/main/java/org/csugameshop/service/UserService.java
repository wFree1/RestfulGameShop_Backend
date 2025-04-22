package org.csugameshop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csugameshop.entity.User;
import org.csugameshop.entity.UserDTO;
import org.csugameshop.mapper.UserMapper;
import org.csugameshop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public User register(UserDTO userDTO) {
        // 校验重复密码
        if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            throw new IllegalArgumentException("前后密码不匹配");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        if (userMapper.selectOne(queryWrapper) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());

        userMapper.insert(user);
        return user;
    }
    public String login(UserDTO userDTO) {
        // 从 Redis 获取验证码
        String redisCaptcha = redisTemplate.opsForValue().get("captcha:" + userDTO.getUsername());
        if (userDTO.getCaptcha() == null || !userDTO.getCaptcha().equalsIgnoreCase(redisCaptcha)) {
            throw new IllegalArgumentException("验证码错误");
        }

        // 验证用户名和密码
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null || !passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 清除 Redis 中的验证码
        redisTemplate.delete("captcha:" + userDTO.getUsername());
        return jwtUtil.generateToken(userDTO.getUsername());
    }

    public User getUserById(int id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return user;
    }

    public User updateUser(int id, UserDTO userDTO) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 校验重复密码（如果提供了新密码）
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
                throw new IllegalArgumentException("前后密码不匹配");
            }
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null && existingUser.getId() != id) {
            throw new IllegalArgumentException("用户名已存在");
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userMapper.updateById(user);
        return user;
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