// 选项式 Store
import {defineStore} from "pinia";
export const useCounterStore = defineStore('counter', {
    state: () => ({ count: 0 }),
    getters: {
        doubleCount: (state) => state.count * 2,
        // 向 getter 传递参数
        // 这样做 getter 将不再被缓存 just a function that you called
        // 但是可以在函数本神做一些缓存
        countPlusN: (state) => {
            return (n) => n + state.count
        },
    },
    actions: {
        // action 函数可以是异步的 且可以调用其他action
        increment() {
            this.count++
        },
    },
})

// 始终尽量不要在同一组件中混合两种 API 样式
