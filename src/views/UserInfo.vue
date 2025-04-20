<template>
    <!-- 用户信息展示 -->
    <div>
      <table class="userinfo-table">
        <tbody>
          <tr>
            <td rowspan="3" class="avatar-cell">
              <img 
                :src="userInfo.avatar || 'images/img.png'" 
                alt="用户头像"
                class="avatar-img"
              />
            </td>
            <td class="label">ID：</td>
            <td class="value">{{ userInfo.id }}</td>
          </tr>
          <tr>
            <td class="label">昵称：</td>
            <td class="value">{{ userInfo.username }}</td>
          </tr>
          <tr>
            <td class="label">邮箱：</td>
            <td class="value">{{ userInfo.email }}</td>
          </tr>
          <tr>
            <td colspan="3" class="action-cell">
              <router-link 
                to="/update-info" 
                class="edit-button"
              >
                编辑信息
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
</template>

<script>

export default {
  name: 'UserInfo',
  data() {
    return {
      userInfo: {
        id: null,
        username: '加载中...',
        email: '加载中...',
        avatar: null
      },
      loading: true,
      error: null
    }
  },
  mounted() {
    this.fetchUserData()
  },
  methods: {
    async fetchUserData() {
      try {
        const response = await this.$axios.get('/api/users/'+userId, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        })
        
        if (response.data.success) {
          this.userInfo = {
            id: response.data.user.id,
            username: response.data.user.username,
            email: response.data.user.email,
            avatar: response.data.user.avatar
          }
        }
      } catch (error) {
        console.error('数据获取失败:', error)
        this.error = '无法获取用户信息'
        this.$notify({
          type: 'error',
          title: '错误',
          message: '用户信息获取失败'
        })
      } finally {
        this.loading = false
      }
    }
  }
}


</script>

<style scoped>
body{
    background-color: #1b2838;
    color: #ffffff;
    margin: 0;
    padding: 0;
}

/*top*/
#logo{
    float: left;
    height: 100px;
    width: 100px;
}
ul.header{
    position: fixed;
    background-color: #171d25;
    list-style-type: none;
    margin: 0;
    padding: 0;
    top: 0;
    width: 100%;
    z-index: 100;
}
li.header{
    display: inline;
    float: left;
    padding-top: 20px;
}
li.header a{
    display: block;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
    font-size: 25px;
}
li.header a:hover{
    color: #0099ff;
}

/*userInfo*/
/*个人信息*/
table.userinfo{
    width: 780px;
    height: 400px;
    background-color: #1a2127;
    color: #a5bec3;
    border-collapse: collapse;
    margin: 100px auto;
}
.userinfo td{
    padding: 28px;
    font-size: 22px;
}
.editBtn{
    width: 100px;
    height: 40px;
    font-size: 15px;
    background-color: #223d57;
    color: #f3f3f3;
    border: 1px solid #1b2838;
    float: right;
    border-radius: 4px;
}
.editBtn:hover{
    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
}
</style>