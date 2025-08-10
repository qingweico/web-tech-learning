<template>
  <h2> {{ cities }}</h2>
  <el-button type="success" text
             bg @click="append">
    添加
  </el-button>
</template>
<script>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import Chance from 'chance'
export default {
  name: 'CompositionOlderApi',
  setup(props, ctx) {
    console.log(props)
    console.log(ctx)
    const cities = ref(['北京', '上海', '苏州'])

    function append() {
      if(cities.value.length >= 5) {
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

    return {
      cities,
      append
    }
  }
}
</script>
