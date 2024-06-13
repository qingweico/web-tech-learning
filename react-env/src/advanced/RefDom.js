// 使用 ref 访问另一个组件的 DOM 节点
// 当 ref 放置在浏览器元素的内置组件上, 会将 ref 设置为相应的 DOM 节点
import {forwardRef, useRef} from 'react';

export default function RefButton() {
    let ref = useRef(null);

    function refButton() {
        console.log(ref.current)
    }

    return (<>
        <button ref={ref}>示例按钮</button>
        <button onClick={refButton}>点击</button>
    </>)
}
// 为了安全, React 不允许组件访问其他组件的 DOM 节点
// fail :  Function components cannot be given refs. Attempts to access this ref will fail
// 但是组件可以主动暴露 ref 访问节点, 让其他组件访问, 使用 forwardRef API

const MyInput = forwardRef((props, ref) => {
    return <input {...props} ref={ref}/>;
});

function MyForm() {
    const inputRef = useRef(null);

    function handleClick() {
        inputRef.current.focus();
    }

    return (<>
        <MyInput ref={inputRef}/>
        <button onClick={handleClick}>
            聚焦输入框
        </button>
    </>);
}

export {MyForm}
