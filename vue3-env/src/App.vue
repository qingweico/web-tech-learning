<template>
  <h1>
    Message : {{ message }}
  </h1>
  <span>{{el}}</span>
  <button @click="clickAlert">Click</button>
  <button @click="changeMessage">Change Message</button>
</template>

<script>

import {ref, reactive} from "vue";

export default {
  name: 'App',
  components: {
  },
  setup(){
    // 定义一个响应式的数据
    let message = ref("This is a message")

    const el = reactive(['e1', 'e2', 'e3'])

    function clickAlert () {
      alert(`${message.value}`)
    }
    function changeMessage () {
      message.value = "This is a changed message"
      el[0] = 'e4'
    }
    return {
      message,
      clickAlert,
      changeMessage,
      el
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
