package org.csugameshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 生成验证码图片
    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateCaptcha(@RequestParam(value = "username", required = false) String username) throws IOException {
        // 如果没有提供用户名，使用一个默认值或会话 ID（这里简化为固定值）
        if (username == null || username.isEmpty()) {
            username = "defaultUser";
        }

        // 生成验证码
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
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    // 验证用户输入的验证码
    @PostMapping("/verify")
    public VerificationResponse verifyCaptcha(@RequestBody VerificationRequest request) {
        String storedCaptcha = redisTemplate.opsForValue().get("captcha:" + request.getUsername());
        boolean isValid = storedCaptcha != null && storedCaptcha.equalsIgnoreCase(request.getCaptcha());
        return new VerificationResponse(isValid);
    }

    // 请求 DTO
    static class VerificationRequest {
        private String username;
        private String captcha;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getCaptcha() { return captcha; }
        public void setCaptcha(String captcha) { this.captcha = captcha; }
    }

    // 响应 DTO
    static class VerificationResponse {
        private boolean valid;
        public VerificationResponse(boolean valid) { this.valid = valid; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
    }
}