<template>
  <div>User Profile</div>
  <router-link :to="{ name: 'settings'}">
    User Settings
  </router-link>
  <br/>
  <router-link :to="{ name: 'center'}">
    User Center
  </router-link>
</template>
<script setup>
/**组件内的守卫*/

// beforeRouteEnter 是支持给 next 传递回调的唯一守卫
import {onBeforeRouteLeave, onBeforeRouteUpdate} from "vue-router";

function onBeforeRouteEnter(param) {

}

// Vue3 组合式API中没有beforeRouteEnter的替代(onBeforeRouteEnter)吗
onBeforeRouteEnter((to, from, next) => {
  // 在渲染该组件的对应路由被验证前调用
  // 不能获取组件实例 `this`
  // 因为当守卫执行时,组件实例还没被创建
  next(vm => {
    // 通过 `vm` 访问组件实例
  })
  console.log('beforeRouteEnter')
})
onBeforeRouteUpdate((to, from) => {
  // 在当前路由改变,但是该组件被复用时调用
  // 举例来说,对于一个带有动态参数的路径 `/users/:id`,在 `/users/1` 和 `/users/2` 之间跳转的时候,
  // 由于会渲染同样的 `UserDetails` 组件,因此组件实例会被复用. 而这个钩子就会在这个情况下被调用.
  // 因为在这种情况发生的时候,组件已经挂载好了,导航守卫可以访问组件实例 `this`
  console.log('beforeRouteUpdate')
})
onBeforeRouteLeave((to, from) => {
  // 在导航离开渲染该组件的对应路由时调用
  // 与 `beforeRouteUpdate` 一样,它可以访问组件实例 `this`
  console.log('beforeRouteLeave')
})
// <router-link></router-link> 组合式函数
import { RouterLink, useLink } from 'vue-router'
import { computed } from 'vue'

const props = defineProps({
  ...RouterLink.props,
  inactiveClass: String,
})

const {
  // 解析出来的路由对象
  route,
  // 用在链接里的 href
  href,
  // 布尔类型的 ref 标识链接是否匹配当前路由
  isActive,
  // 布尔类型的 ref 标识链接是否严格匹配当前路由
  isExactActive,
  // 导航至该链接的函数
  navigate
} = useLink(props)

const isExternalLink = computed(
    () => typeof props.to === 'string' && props.to.startsWith('http')
)
console.log(props)


</script>
