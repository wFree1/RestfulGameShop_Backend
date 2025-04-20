<template>
    <div class="w">
      <div class="user-content">
        <div class="user-title">
          <img src="@/assets/logo.png" alt="logo" class="user-title-logo">
          User_Update
        </div>
  
        <!-- 错误提示 -->
        <div v-if="errorMsg" class="user-form-error">
          <i class="fa-solid fa-circle-exclamation error-icon"></i>
          <p class="error-msg">{{ errorMsg }}</p>
        </div>
  
        <form @submit.prevent="handleSubmit">
          <!-- 用户名 -->
          <div class="user-form-item">
            <label for="username" class="user-form-label">
              <i class="fa-solid fa-user"></i>
            </label>
            <input
              type="text"
              class="user-form-input"
              v-model="formData.username"
              placeholder="请输入新用户名"
              @blur="validateUsername"
            >
          </div>
          <div id="username-feedback" class="feedback">{{ usernameError }}</div>
  
          <!-- 密码 -->
          <div class="user-form-item">
            <label for="password" class="user-form-label">
              <i class="fa-solid fa-lock"></i>
            </label>
            <input
              type="password"
              class="user-form-input"
              v-model="formData.password"
              placeholder="请输入新密码"
              @blur="validatePassword"
            >
          </div>
          <div id="password-feedback" class="feedback">{{ passwordError }}</div>
  
          <!-- 重复密码 -->
          <div class="user-form-item">
            <label for="repeatPassword" class="user-form-label">
              <i class="fa-solid fa-lock"></i>
            </label>
            <input
              type="password"
              class="user-form-input"
              v-model="formData.repeatPassword"
              placeholder="请再次输入密码"
              @blur="validateRepeatPassword"
            >
          </div>
          <div id="repeatPassword-feedback" class="feedback">{{ repeatPasswordError }}</div>
  
          <div class="user-form-item">
            <button 
              type="submit" 
              class="user-form-submit"
              :disabled="isSubmitting"
            >
              {{ isSubmitting ? '提交中...' : '确认修改' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import { mapState } from 'vuex'
  import { validateUsername, validatePassword } from '@/utils/validators'
  
  export default {
    name: 'UserUpdate',
    data() {
      return {
        formData: {
          username: '',
          password: '',
          repeatPassword: ''
        },
        usernameError: '',
        passwordError: '',
        repeatPasswordError: '',
        errorMsg: '',
        isSubmitting: false
      }
    },
    computed: {
      ...mapState(['currentUser'])
    },
    methods: {
      // 验证逻辑
      validateUsername() {
        this.usernameError = validateUsername(this.formData.username)
        return !this.usernameError
      },
      validatePassword() {
        this.passwordError = validatePassword(this.formData.password)
        return !this.passwordError
      },
      validateRepeatPassword() {
        if (this.formData.password !== this.formData.repeatPassword) {
          this.repeatPasswordError = '两次输入的密码不一致'
          return false
        }
        this.repeatPasswordError = ''
        return true
      },
      validateForm() {
        return [
          this.validateUsername(),
          this.validatePassword(),
          this.validateRepeatPassword()
        ].every(v => v)
      },
  
      // 提交处理
      async handleSubmit() {
        if (!this.validateForm() || this.isSubmitting) return
  
        this.isSubmitting = true
        this.errorMsg = ''
  
        try {
          const response = await this.$axios.put('/api/users/'+userId, {
            userId: this.currentUser.id,
            ...this.formData
          })
  
          if (response.data.success) {
            this.$notify({
              type: 'success',
              title: '更新成功',
              message: '用户信息已更新'
            })
            await this.$store.dispatch('fetchUserInfo')
            this.$router.push('/userinfo')
          } else {
            this.errorMsg = response.data.message || '更新失败'
          }
        } catch (error) {
          console.error('更新请求失败:', error)
          this.errorMsg = '网络错误，请稍后重试'
        } finally {
          this.isSubmitting = false
        }
      }
    },
    mounted() {
      // 初始化当前用户名
      this.formData.username = this.currentUser.username
    }
  }
  </script>
  
  <style scoped>
  /* 复用原有样式，新增以下样式 */
  .user-form-error {
    padding: 10px;
    margin: 15px 0;
    background: #ffe0e0;
    border-radius: 4px;
    display: flex;
    align-items: center;
  }
  
  .error-icon {
    color: #ff4444;
    margin-right: 8px;
  }
  
  .error-msg {
    color: #ff4444;
    margin: 0;
  }
  
  button[disabled] {
    opacity: 0.7;
    cursor: not-allowed;
  }
  
  .feedback {
    color: #ff4444;
    font-size: 14px;
    margin: 5px 0 15px 40px;
  }
  </style>