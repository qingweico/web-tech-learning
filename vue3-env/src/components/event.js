// event.js
import { onMounted, onUnmounted } from 'vue'

export function useEventListener(target, event, callback) {
    // 如果你想的话,
    // 也可以用字符串形式的 CSS 选择器来寻找目标 DOM 元素
    onMounted(() => target.addEventListener(event, callback))
    onUnmounted(() => target.removeEventListener(event, callback))
}

export function useEventListenerSelector(selector, event, callback) {
    let target = null;

    const getTarget = () => {
        if (typeof selector === 'string') {
            target = document.querySelector(selector);
        } else {
            target = selector;
        }
        return target;
    };

    onMounted(() => {
        const element = getTarget();
        if (element) {
            element.addEventListener(event, callback);
        }
    });

    onUnmounted(() => {
        const element = getTarget();
        if (element) {
            element.removeEventListener(event, callback);
        }
    });
}
