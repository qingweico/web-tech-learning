// Hook useMemo
// 记住函数的返回值, 并在依赖项没有发生变化时返回缓存的值,减少不必要的计算
import {useState, useMemo} from 'react';


export default function MemeApp() {
    const [count, setCount] = useState(10);
    const [num, setNum] = useState(0);

    function complexCalculate(num: number) {
        let sum = 1;
        for (let i = 0; i < num; i++) {
            sum += (Math.PI + Math.E) / sum;
        }
        console.log('complexCalculate')
        return Number(sum).toFixed(3);
    }


    const result = useMemo(() => complexCalculate(count), [count]);
    return (
        <div>
            <h1>循环次数 {count}</h1>
            <h1>计算结果 {result}</h1>
            <button onClick={() => setCount(count + 1)}>重新计算</button>
            <button onClick={() => setNum(num + 1)}>重新渲染</button>
        </div>
    )
}
