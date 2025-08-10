<template>
  <h2> {{ cities }}</h2>
  <el-button type="success" text
             bg @click="append">
    添加
  </el-button>
</template>
<script setup>
import {onMounted, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import Chance from 'chance'

const cities = ref(['北京', '上海', '苏州'])

function append() {
  if (cities.value.length >= 5) {
    ElMessage({
      message: '最多添加5个城市.',
      type: 'error',
    })
    return
  }
  const chance = new Chance();
  const randomCity = chance.city();
  cities.value.push(randomCity)
}
onMounted(() => {
  {
    console.log('mounted')
    const proxy = {}
    console.log(reactive(proxy) === proxy)
    // 对一个已存在的代理对象调用 reactive() 会返回其本身
    // reactive不能持有原始类型
    console.log(reactive(reactive(proxy)) === reactive(proxy))
  }
})

</script>
