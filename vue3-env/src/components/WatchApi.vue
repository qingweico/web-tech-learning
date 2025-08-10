<template>
  <div>
    <p>x: {{x}}</p>
    <button @click="x++">x++</button>
    <p>y: {{y}}</p>
    <button @click="y++">y++</button>
    <p>Count: {{obj.count}}</p>
    <button @click="obj.count++">count++</button>
  </div>

</template>

<script setup>
import {defineComponent, reactive, ref, watch} from "vue";
import {ElNotification} from "element-plus";

const x = ref(0)
const y = ref(0)


// watch(x, (newX) => {
//   ElNotification({
//     title: '提示',
//     message: `x is ${newX}`,
//     type: 'success',
//   })
// })
//
// // getter 函数
// watch(
//     () => x.value + y.value,
//     (sum) => {
//       ElNotification({
//         title: '提示',
//         message: `sum of x + y is: ${sum}`,
//         type: 'success',
//       })
//     }
// )
const obj = reactive({ count: 0 })
// watch([x, () => y.value], ([newX, newY]) => {
//   ElNotification({
//     title: '提示',
//     message: `x is ${newX} and y is ${newY}`,
//     type: 'success',
//   })
// })

// 监听嵌套对象中的属性 使用getter 函数 而非key

watch(
    () => obj.count,
    (count) => {
      ElNotification({
        title: '提示',
        message: `Count is: ${count}`,
        type: 'success',
      })
    },
    // 当 `source` 变化时,仅触发一次
    { deep: true,
      immediate: true,
      once: true})

defineComponent({
  name: 'WatchApi',
})
</script>
