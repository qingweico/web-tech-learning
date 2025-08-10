<template>
  <h1>ReactiveApi</h1>
  <div>当前分数: {{ score }}</div>
  <div>计算属性: {{ passed }}</div>
  <el-button type="success" text
             bg @click="changeToNotPassed">修改为不通过</el-button>
</template>
<script setup>
/**
 * 修改计算属性 (可以实现 但是不要这么做)
 */
import {computed, onMounted, reactive, ref} from "vue";

const score = ref(60)
const passed = computed({
  get() {
    return score.value >= 60 ? '通过' : '不通过'
  },
  set(newValue) {
    if (newValue >= 0 && newValue <= 100) {
      score.value = Number(newValue)
    }

  }
})
const changeToNotPassed = () => {
  passed.value = String(Math.floor(Math.random() * 60))
}
onMounted(() => {
  {
    const books = reactive([ref('Vue 3 Guide')])
    // 这里需要 .value
    // Vue 3 Guide
    console.log(books[0].value)

    const map = reactive(new Map([['count', ref(0)]]))
    // 这里需要 .value
    // 0
    console.log(map.get('count').value)
  }
})

</script>
