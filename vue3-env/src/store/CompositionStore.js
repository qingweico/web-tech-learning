import {defineStore} from 'pinia'
import {computed, ref} from "vue";

export const useCounterStore = defineStore('counter', () => {
    // 对比声明式API  ref 就是state属性
    const count = ref(0)
    const cities = ref([])
    const tenIncrement = () => {
        count.value = count.value * 10
    }
    const doubleIncrement = () => {
        count.value = count.value * 2
    }
    const increment = () => count.value++
    const decrement = () => count.value--
    // function 函数就是 action
    function rdmIncr() {
        count.value = Math.ceil(Math.random() * 10) + count.value
    }
    const resetToZero = () => count.value = 0
    // computed() 就是getter
    const getCounter = computed(() => count.value)

    const getCities = computed(() => cities.value)
    // 在 setup 模式中必须返回所有的属性
    return {count, tenIncrement, doubleIncrement, increment, decrement, resetToZero, getCounter, getCities, rdmIncr}
})
