<template>
  <div :style="{ 'font-size': fontSize + 'px' }">{{ msg }}
    <slot></slot>
    <p> info : {{ info }} </p>
    <p> name : {{ name }} </p>
    <p> code : {{ code }} </p>
    <el-button type="success" text
               bg @click="editParentProps">修改父组件的Props</el-button>
  </div>
  <el-button type="success" text
             bg @click="$emit('enlarge-text')">Enlarge text</el-button>
  <div>Parent bound v-model is: {{ model }}</div>
  <el-button type="success" text
             bg @click="update">Increment</el-button>

</template>

<script setup>
import {ref} from "vue";

const msg = ref('Child')
defineExpose({
  msg
})
defineProps(['fontSize', 'name', 'code', 'info'])
const emit =  defineEmits(['enlarge-text', 'update-info'])

const editParentProps = () => {
  emit('update-info', { name: 'new info', code: 'new code' });
}
const model = defineModel()
function update() {
  model.value++
}

</script>
