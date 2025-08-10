<template>
  <div>
    <slot></slot>
    <input ref="input"/>

    <br/>
    <p>itemRefs: {{ itemRefs }}</p>
    <ul>
      <li v-for="(item, index) in list" ref="itemRefs" :key="index">
        {{ item }}
      </li>
    </ul>


    <br/>

    <input :ref="(el) => {
      bindRef = el
     }">
    <button v-if="bindRef" @click="unmountInput">Unmount Input Node</button>

    <br/>
    <ChildrenPage ref="child" @enlarge-text="fontSize += 1" :font-size="fontSize" v-bind="info" :info="info"
                  @update-info="updateInfo">
      这是父组件的消息
    </ChildrenPage>
    <h2>AppCode: {{ getAppCode() }}</h2>
    <h2>Grade: {{ grade }}</h2>
    <h2>From: {{ form }}</h2>
  </div>

</template>


<script setup>
import {ref, onMounted, watchEffect, nextTick} from 'vue'
import {ElMessage} from "element-plus";
import ChildrenPage from "@/components/ChildrenPage.vue";

defineProps({
  id: {
    type: [String, null],
    required: false
  },
  form: {
    type: Object,
    required: false,
    //default: () => {},
    default(rawProps) {
      return { message: 'Prefix_' + rawProps['grade'], type: 'Prefix_' + rawProps['grade'] }
    }
  },
  getAppCode: {
    type: Function,
    default() {
      return '00XN';
    }
  },
  grade: {
    type: String,
    validator(value, props) {
      return ['A', 'B', 'C', 'D', 'E'].includes(value)
    }
  }
})
const input = ref(null)
const bindRef = ref(null)
const child = ref(null)
const fontSize = ref(16)
onMounted(() => {
  // input.value.focus()
  console.log(child.value)
})
watchEffect(() => {
  if (input.value) {
    input.value.focus()
  }
})

const list = ref([
  'Beijing',
  'Shanghai',
  'Guangzhou'
])

const itemRefs = ref([])

const unmountInput = () => {
  if (bindRef.value) {
    const parent = bindRef.value.parentNode
    if (parent) {
      parent.removeChild(bindRef.value)
      nextTick(() => {
        bindRef.value = null
      })
    }
  } else {
    ElMessage.error('bindRef.value is null')
  }
}


let info = ref({
  name: 'Vue3',
  code: 'Code-Helper'
})


const updateInfo = (newInfo) => {
  info.value = { ...info.value, ...newInfo };
}
</script>
