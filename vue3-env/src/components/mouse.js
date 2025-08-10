// 组合式函数名以“use”开头
import { useEventListener, useEventListenerSelector } from './event'
import { ref } from 'vue'
export function useMouse() {
    const x = ref(0)
    const y = ref(0)

    useEventListenerSelector(window, 'mousemove', (event) => {
        x.value = event.pageX
        y.value = event.pageY
    })

    return { x, y }
}
