<template>
  <div>
    <el-text size="large">count: {{ count }}</el-text>
    <br/>
    <el-text size="large">getCounter: {{ getCounter }}</el-text>
    <br/>
    <el-text size="large">countRef: {{ countRef }}</el-text>
    <br/>
    <el-button type="default" @click="increment">加一</el-button>
    <el-button type="primary" @click="decrement">减一</el-button>
    <el-button type="info" @click="doubleIncrement">乘2</el-button>
    <el-button type="warning" @click="tenIncrement">乘10</el-button>
    <el-button type="danger" @click="resetToZero">归零</el-button>
    <el-divider/>
    <el-button type="success" text
               @click="f1"
               bg>直接操作 state 中的数据
    </el-button>
    <el-button type="success" text
               @click="f2"
               bg>使用 mutations
    </el-button>
    <el-button type="success" text
               @click="f3"
               bg>使用 action 代替
    </el-button>

  </div>
</template>
<script setup>
import { defineComponent } from 'vue'
import {useCounterStore} from '@/store/CompositionStore'
import {mapActions, mapState, mapStores, storeToRefs} from 'pinia'
import {computed} from "vue";
// 可以在组件中的任意位置访问 `store` 变量 ✨
const counter = useCounterStore()
const store = useCounterStore()
// ✅ 这样写是响应式的
// 💡 当然你也可以直接使用 `store.doubleCount`
// const count = computed(() => store.count)

// store 对象是 reactive 包裹的对象 一旦解构就会破环了响应性
// 为了从 store 中提取属性时保持其响应性, 你需要使用 storeToRefs()
const {count: countRef} = storeToRefs(store)
// 直接操作 state 中的数据
const f1 = () => counter.count++
// 使用【基于对象的补丁更新】部分更新状态的官方推荐方式之一, 可以传递一个对象, 也可以传一个函数
const f2 = () => counter.$patch({count: counter.count + 1})
// 或使用 action 代替
const f3 = () => counter.increment()

const {tenIncrement, doubleIncrement, increment, decrement, resetToZero} = store
const getCounter = computed(() => countRef.value)
defineComponent({
      computed: {
        ...mapState(useCounterStore, ['count']),
      },
      methods: {
        ...mapActions(useCounterStore, ['increment']),
      },
    }
)
</script>
