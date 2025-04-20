import { createApp } from 'vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue';

createApp(Login).mount('#login');
createApp(Register).mount('#register');
