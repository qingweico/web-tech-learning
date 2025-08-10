<template>
  <el-button type="success" text
             bg @click="changeUrl">修改</el-button>
  <div v-if="!err.value" v-color="'blue'"> {{ text }}
  </div>
  <div v-color="'red'" v-else>Error Message : {{ err }}</div>
</template>
<script setup>
import {useFetch} from "@/components/fetch";
import {onMounted, ref} from "vue";

const text = ref('')
const err = ref('')
const id =ref('1')
const changeUrl = () => {
  id.value++
}
onMounted(() => {
  const {data, error} = useFetch(() => `/api/mock/get/${id.value}`)
  text.value = data
  err.value = error
})

</script>
