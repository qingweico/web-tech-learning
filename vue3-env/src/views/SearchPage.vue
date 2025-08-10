<template>
  <div class="search-container">
    <el-input
        v-model="searchQuery"
        placeholder="请输入搜索内容"
        class="search-input"
        @keyup.enter="handleSearch"
    >
      <template #append>
        <el-button class="search-button" type="primary" @click="handleSearch">
          搜索
        </el-button>
      </template>
    </el-input>
    <p>通过 route query 参数转换为 props: {{ query }}</p>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRouter, useRoute} from 'vue-router'
defineProps({
  query: String
})
const searchQuery = ref('');
const router = useRouter()
const route = useRoute()
onMounted(() => {
  if (route.query) {
    searchQuery.value = route.query['q']
  }
  // 检测重定向
  // 重定向不会阻止导航, 而是创建一个新的导航
  // 通过读取路由地址中的 redirectedFrom 属性
  // 可以判断当前route是否通过别的route重定向过来的
  if (router.currentRoute.value.redirectedFrom) {
    console.log(router.currentRoute.value.redirectedFrom)
  }
})
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    window.location.href = `https://www.baidu.com/s?wd=${encodeURIComponent(searchQuery.value)}`;
  }
}
</script>

<style scoped>
.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #fff;
}

.search-input {
  width: 500px;
  border-radius: 24px;
}

.search-button {
  background-color: #409EFF;
  color: white;
  border-radius: 0 24px 24px 0;
  border: none;
}

.search-input .el-input__inner {
  border-radius: 24px 0 0 24px;
  border-right: none;
  height: 48px;
  font-size: 16px;
  padding-right: 10px;
}

.search-button:hover {
  background-color: #337ecc;
}
</style>
