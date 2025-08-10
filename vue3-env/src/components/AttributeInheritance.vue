<template>
  <div>
    <AttrChildren v-bind="$attrs" @click="parentClick"></AttrChildren>
    <div style="height: 20px"></div>
    <AttrPlaceholder/>
  </div>
</template>

<script setup>
import {ElMessage} from "element-plus";
import AttrChildren from "@/components/AttrChildren.vue";
import AttrPlaceholder from "@/components/AttrPlaceholder.vue";
const parentClick = () => {
  ElMessage({
    message: 'parent click',
    type: 'success',
  })
}
</script>
