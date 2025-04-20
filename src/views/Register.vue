<template>
    <div class="w">
    <div class="user-content">
        <div class="user-title">
            <img src="/src/assets/logo.png" alt="logo" id="logo"  class="user-title-logo">
            GameShop_Register
        </div>
        <form action="register" class="user-form" method="post">
            <p class="login-tip">用户注册</p>

            <div class="user-form-item">
                <label for="username" class="user-form-label">
                    <i class="fa-solid fa-user"></i>
                </label>
                <input  v-modle="username"
                        type="text" 
                        class="user-form-input" 
                        name="username" 
                        id="username" 
                        placeholder="请输入用户名" 
                        autocomplete="off">
            </div>
            <div id="username-feedback" class="feedback"></div>

            <div class="user-form-item">
                <label for="password" class="user-form-label">
                    <i class="fa-solid fa-lock"></i>
                </label>
                <input  v-model="password"
                        type="password" 
                        class="user-form-input" 
                        name="password" 
                        id="password" 
                        placeholder="请输入密码" 
                        autocomplete="off">
            </div>
            <div id="password-feedback" class="feedback"></div>

            <div class="user-form-item">
                <label for="repeatPassword" class="user-form-label">
                    <i class="fa-solid fa-lock"></i>
                </label>
                <input  v-model="repeatPassword"
                        type="password" 
                        class="user-form-input" 
                        name="repeatPassword" 
                        id="repeatPassword" 
                        placeholder="请再次输入密码" 
                        autocomplete="off">
            </div>
            <div id="repeatPassword-feedback" class="feedback"></div>

            <div class="user-form-item">
                <label for="email" class="user-form-label">
                    <i class="fa-solid fa-envelope"></i>
                </label>
                <input  v-model="email"
                        type="text" 
                        class="user-form-input" 
                        name="email" 
                        id="email" 
                        placeholder="请输入邮箱" 
                        autocomplete="off">
            </div>
            <div id="email-feedback" class="feedback"></div>
            <br>

            <div class="user-form-item">
                <input type="submit" value="注册" class="user-form-submit">
            </div>
            <div class="user-form-link">
                <a href="/login.html" class="link">返回登录>></a>
            </div>
        </form>
    </div>
</div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Register",
  data() {
    return {
      username: '',
      password: '',
      repeatPassword: '',
      email: '',
    };
  },
  methods: {

    // 表单验证方法
    validateForm() {
      let isValid = true;

      // 用户名验证
      const usernameError = checkUsername(this.username);
      if (usernameError) {
        this.setFeedback('username-feedback', usernameError);
        isValid = false;
      }

      // 密码验证
      const passwordError = checkPassword(this.password);
      if (passwordError) {
        this.setFeedback('password-feedback', passwordError);
        isValid = false;
      }

      // 重复密码验证
      if (this.password !== this.repeatPassword) {
        this.setFeedback('repeatPassword-feedback', '两次密码输入不一致');
        isValid = false;
      }

      // 邮箱验证
      const emailError = this.validateEmail(this.email);
      if (emailError) {
        this.setFeedback('email-feedback', emailError);
        isValid = false;
      }

      return isValid;
    },

    // 统一设置反馈信息
    setFeedback(elementId, message) {
      document.getElementById(elementId).textContent = message;
    },

    // 邮箱验证方法
    validateEmail(email) {
      if (!email) return '邮箱不能为空';
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) return '邮箱格式不正确';
      return '';
    },

    // 表单提交处理
    async handleSubmit(e) {
      e.preventDefault();
      
      if (!this.validateForm()) return;

      try {
        const response = await axios.post('/api/users/register', {
          username: this.username,
          password: this.password,
          email: this.email,
          captcha: this.captchaInput
        });

        if (response.data.success) {
          alert('注册成功，即将跳转到登录页面');
          this.$router.push('/login');
        } else {
          this.errorMsg = response.data.message;
          this.refreshCaptcha();
        }
      } catch (error) {
        this.errorMsg = '注册失败，请稍后重试';
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

.w{
width: 1080px;
height: 500px;
margin: 0 auto;
position: relative;
overflow: hidden;
background: #1b2838;
padding: 50px 0;
}

.user-content{
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

.user-title{
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
display: flex; align-items: center;
}

.user-title-logo{
height:  75px;
}
.user-form{
padding:20px;
}

.feedback {
color: #fff;
background-image: url('../images/warning.png');
background-repeat: no-repeat;
background-position: 2px 14px;
padding: 10px 0 0 22px;
}

.user-form-item{
position: relative;
margin-top: 20px;
}
.user-form-input{
padding: 10px 0 10px 50px;
width: 300px;
height: 1px;
line-height: 28px;
font-size: 15px;
border: 1px solid #bdbdbd;
outline: none;
}
.user-form-label{
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

.user-form-submit{
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

.user-form-link{
text-align: right;
margin-right: 5px;
}
.link{
color: #999;
cursor: pointer;
font-size: 13px;
}
.link:hover{
color: #215496;
}

.user-form-error{
width: 300px;
position: relative;
padding: 4px 0 4px 40px;
color: red;
border: 1px solid red;
font-size: 14px;
margin-bottom: 10px;
}
.error-icon{
position: absolute;
left: 14px;
top: 50%;
margin-top: -7px;
}
</style>