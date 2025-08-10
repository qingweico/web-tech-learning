<template>
  <div class="logout-container">
    <el-button type="primary" @click="logout">退出登录</el-button>
  </div>
</template>

<script setup>
import {useRouter} from "vue-router";
import {useAuthStore} from "@/store/authStore";
import {ElMessageBox} from "element-plus";
import { NavigationFailureType, isNavigationFailure } from 'vue-router'
const authStore = useAuthStore();
const router = useRouter();
const logout = () => {
  ElMessageBox.confirm(
      '确认退出登录吗?',
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        authStore.logout()
        // router.push('/login');
        // router.push({path: '/login'});
        const failure = await router.push({name: 'Login'})
        // 导航故障类型
        if (isNavigationFailure(failure, NavigationFailureType.aborted)) {
          // aborted: 在导航守卫中返回 false 中断了本次导航
          // cancelled: 在当前导航完成之前又有了一个新的导航
          // duplicated: 导航被阻止, 已经在目标位置
          const {to, from} = failure
          console.log(`route nav failure[aborted], ${from}, ${to}`)
        }
      })
      .catch(() => {
      })

}
</script>

<style scoped>
.logout-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
}
</style>
