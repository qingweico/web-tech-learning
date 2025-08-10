<template>
  <div class="btn-wrapper">
    <p>Fallthrough attribute: {{ $attrs }}</p>
    <el-button type="success" text
               bg class="child" v-bind="$attrs" @click="parentClick">parentClick</el-button>
  </div>
</template>

<script setup>
// 不想要一个组件自动地继承 父组件的 attribute
defineOptions({
  inheritAttrs: false
})
import { useAttrs } from 'vue'

const attrs = useAttrs()
console.log(attrs['class'])
console.log(attrs['onClick'])
import {ElMessage} from "element-plus";

const parentClick = () => {
  ElMessage({
    message: 'child click',
    type: 'success',
  })
}
</script>
