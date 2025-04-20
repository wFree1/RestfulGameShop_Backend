<template>
    <div class="w">
    <div class="user-content">
        <div class="user-title">
            <img src="/src/assets/logo.png" alt="logo" id="logo"  class="user-title-logo">
            GameShop_Login
        </div>
        <form action="login" class="user-form" method="post">
            <p class="login-tip">用账户名称登录</p>
            
            <!-- username -->
            <div class="user-form-item">
                <label for="username" class="user-form-label">
                    <i class="fa-solid fa-user"></i>
                </label>
                <input v-model="username"
                       type="text" 
                       class="user-form-input" 
                       name="username" id="username" 
                       placeholder="请输入用户名" 
                       autocomplete="off">
            </div>

            <!-- password -->
            <div class="user-form-item">
                <label for="password" class="user-form-label">
                    <i class="fa-solid fa-lock"></i>
                </label>
                <input v-model="password"
                       type="password" 
                       class="user-form-input" 
                       name="password" id="password" 
                       placeholder="请输入密码" 
                       autocomplete="off">
            </div>
            <br>

        <!-- captcha -->
        <div class="captcha-container">
            <input  v-model="captchaInput"
                    type="text" 
                    class="captcha-input"
                    placeholder="请输入验证码"
                    autocomplete="off"
            />
            <img 
                    :src="captchaUrl" 
                    alt="验证码" 
                    class="captcha-image"
                    @click="refreshCaptcha"
            />
        </div>

        <!-- 错误提示（Vue版） -->
        <div v-if="errorMsg" class="feedback">
            <p class="error-msg">{{ errorMsg }}</p>
        </div>
            <div class="user-form-item">
                <input type="submit" value="登录" class="user-form-submit">
            </div>
            <div class="user-form-link">
                <a href="/register.html" class="link">还没有GameShop账户？点击这里</a>
            </div>
    </form>
    </div>
</div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Login",
  data() {
    return {
      username: '',
      password: '',
      captchaInput: '',
      captchaUrl: '/api/captcha?' + Date.now(), // 初始验证码URL
      errorMsg: ''
    };
  },
  methods: {
    refreshCaptcha() {
      this.captchaUrl = `/api/captcha?${Date.now()}`;
    },
    async handleSubmit(e) {
      e.preventDefault();
      
      try {
        const response = await axios.post('/api/users/login', {
          username: this.username,
          password: this.password,
          captcha: this.captchaInput
        });

        if (response.data.success) {
          // 登录成功处理
          this.$router.push('/dashboard');
        } else {
          this.errorMsg = response.data.message;
          this.refreshCaptcha();
        }
      } catch (error) {
        this.errorMsg = '请求失败，请检查网络';
        this.refreshCaptcha();
      }
    }
  }
};
</script>

<style scoped>
body {
    background-color: #1b2838;
    color:#ffffff;
    /*font:12px/1.5 ; !*系统默认字体形式大小颜色*!*/
    padding: 50px 0;
}
p {
    padding: 0;
    margin: 0;
}
.w {
    width: 1080px;
    height: 500px;
    margin: 0 auto;
    position: relative;
    overflow: hidden;
    background: #1b2838;
    padding: 50px 0;
}
.user-content {
    position: relative;
    margin: 0 auto;
    background: #171d25;
    width: 400px;
    align-items: center;
    justify-content: start;
    padding: 10px 20px; /* 内边距 */
    border-radius: 15px; /* 设置圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 阴影 */
    border: 1px solid rgba(255, 255, 255, 0.1); /* 边框颜色（透明） */
}
.user-title {
    position: relative;
    text-align: left;
    font-family:inherit;
    font-style: italic;
    padding: 10px 0;
    text-align: center;
    font-size: 30px;
    font-weight: bold;
    color: #fff;
    border-bottom: 1px solid #999;
    display: flex; 
    align-items: center;
}
.user-title-logo {
    height:  75px;
}
.captcha-container {
    display: flex; /* 启用 Flexbox 布局 */
    align-items: center; /* 垂直居中 */
    gap: 10px; /* 输入框和图片之间的间距 */
}
.captcha-input {
    height: 20px; /* 设置输入框的高度 */
    padding: 5px; /* 输入框内边距 */
    font-size: 16px; /* 字体大小 */
    flex: 1; /* 输入框自适应宽度 */
}
.captcha-image {
    width: 100px; /* 验证码图片宽度 */
    height: auto; /* 保持图片宽高比 */
}
.user-form {
    padding:20px;
}
.user-form-item {
    position: relative;
    margin-top: 20px;
}
.user-form-input {
    padding: 10px 0 10px 50px;
    width: 300px;
    height: 1px;
    line-height: 28px;
    font-size: 15px;
    border: 1px solid #bdbdbd;
    outline: none;
}
.user-form-label {
    position: absolute;
    left: 1px;
    top: 1px;
    bottom: 1px;
    width: 30px;
    line-height: 22px;
    background: #f3f3f3;
    color: #d3d3d3;
    text-align: center;
    border-right: 1px solid #bdbdbd;
}
.user-form-submit {
    width: 200px;
    font-size: 20px;
    margin-left: 80px;
    padding: 2px 0 40px;
    font-weight: bold;
    height: 40px;
    line-height: 40px;
    border: none;
    background: #215496;
    color: #ffffff;
    align-items: center;
    justify-content: start;
    padding: 0px 20px; /* 内边距 */
    border-radius: 15px; /* 设置圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 阴影 */
    border: 1px solid rgba(255, 255, 255, 0.1); /* 边框颜色（透明） */
}
.user-form-link {
    text-align: right;
    margin-right: 5px;
}
.link {
    color: #999;
    cursor: pointer;
    font-size: 13px;
}
.link:hover {
    color: #215496;
}
.user-form-error {
    width: 300px;
    position: relative;
    padding: 4px 0 4px 40px;
    color: red;
    border: 1px solid red;
    font-size: 14px;
    margin-bottom: 10px;
}
.error-icon {
    position: absolute;
    left: 14px;
    top: 50%;
    margin-top: -7px;
}
</style>