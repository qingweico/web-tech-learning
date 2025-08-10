<template>
  <div>
    <h3>User Home</h3>
    <p>用户ID : {{ $route.params.id }}</p>
    <p>用户ID : {{ id }}</p>
    <router-view />
  </div>

</template>
<script setup>
import {watch} from 'vue'
import {onBeforeRouteUpdate, useRoute} from 'vue-router'

const route = useRoute()
defineProps({
  id: String
})
watch(() => route.params.id, (newId) => {
  // console.log(newId)
  // ElNotification({
  //   title: '提示',
  //   message: `${newId}`,
  //   type: 'success',
  // })
}, { deep: true,
  immediate: true})
onBeforeRouteUpdate(async (to, from) => {
  // 对路由变化做出响应...
  console.log(`路由从${to.path}变到${from.path}`)

})

</script>
