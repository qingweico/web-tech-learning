// 使用 ref
// ref 是一个普通的 JavaScript 对象
// 使用 ref.current 获取当前 ref 的值, 既可以被读取也可以被修改
import {useRef} from 'react';

export default function Counter() {
    let ref = useRef(0);

    function handleClick() {
        // useRef 值的改变不会引发组件的重新渲染 (希望组件重新渲染可以使用 userState 替代)
        // 使用 useRef 的场景: 适合用于需要保持跨渲染周期不变的可变值
        // 1 : 保存DOM节点的引用
        // 2 : 保存计时器ID
        // 3 : 其他不需要重新渲染的值
        // {@link FocusInput}
        ref.current += 1;
        console.log(`点击了 ${ref.current} 次`);
    }

    return (<button onClick={handleClick}>点击</button>)

}
