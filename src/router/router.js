// router.js
import Vue from 'vue'
import Router from 'vue-router'
import UserInfo from './components/UserInfo.vue'
import UpdateInfo from './components/UpdateInfo.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/userinfo', component: UserInfo },
    { path: '/update-info', component: UpdateInfo }
  ]
})