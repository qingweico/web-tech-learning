<template>
  <div class="login-container">
    <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-position="left"
        label-width="0px"
        class="login-form"
    >
      <h3 class="title">用户登录</h3>

      <el-form-item prop="username">
        <el-input v-model="loginForm.username" placeholder="用户名"/>
      </el-form-item>

      <el-form-item prop="password" >
        <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            @keyup.enter="submitForm"
        />
      </el-form-item>

      <el-form-item>
        <el-button
            :loading="loading"
            type="primary"
            @click="submitForm"
            class="submit-button"
        >
          登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import {useAuthStore} from "@/store/authStore";
import {useRouter, useRoute} from "vue-router";
const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();
// 表单数据
const loginForm = ref({
  username: 'admin',
  password: '123456'
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 15, message: '用户名长度在3到15个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6个字符', trigger: 'blur' }
  ]
}

// 加载状态
const loading = ref(false)
const loginFormRef = ref(null)
// 提交表单
const submitForm = () => {
  loginFormRef.value.validate((valid) => {
    if(valid) {
      // 这里可以添加登录逻辑
      loading.value = true

      // 模拟登录请求
      setTimeout(async () => {
        loading.value = false
        ElMessage.success('登录成功')
        await authStore.login();
        // 获取 redirect 参数, 若不存在则跳转到首页
        const redirect = route.query.redirect || '/';
        // 跳转到目标页面
        await router.push(redirect);
      }, 1000)
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 52px);
  background-color: #f5f7fa;
}

.login-form {
  width: 400px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.submit-button {
  width: 100%;
}
</style>
