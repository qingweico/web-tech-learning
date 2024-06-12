import {useState} from "react";

export default function Form() {
    const [isSent, setIsSent] = useState(false);
    const [count, setCount] = useState(0);
    const [message, setMessage] = useState("This is Message");
    if (isSent) {
        return <h1>你的消息已发送</h1>
    }
    return (<>
        <form onSubmit={e => {
            e.preventDefault();
            // 一个 state 变量的值永远不会在一次渲染的内部发生变化
            setIsSent(true)
            // 此时虽然显示调用setIsSent将isSent设置为true, 只是放入渲染队列中
            // isSent 仍然为 false,无需担心运行时state的值发生了变化
            sendMessage(message)
        }}
        >
        <textarea placeholder="Message" value={message} onChange={e => setMessage(e.target.value)}>
        </textarea>
            <button type="submit">Send</button>
        </form>

        <br/>
        <span style={{'color': 'black'}}>{count}</span>
        <button onClick={e => {
            // 每次只会加一的原因:
            // 1: state变量在每次渲染的内部使用快照副本
            // 2: 像 VUE 一样, 每次函数执行完后才会重新渲染组件(批处理)
            setCount(count + 1);
            setCount(count + 1);
            setCount(count + 1);
        }}>点我加一
        </button>
        <br/>
        <span style={{'color': 'red'}}>{count}</span>
        <button onClick={e => {
            // 串联批处理, 每次使用最新的状态值
            // 根据队列中的前一个 state 计算下一个 state
            setCount(n => n + 1);
            setCount(n => n + 1);
            setCount(n => n + 1);
        }}>点我加三
        </button>
        <br/>
        <span style={{'color': 'red'}}>{count}</span>
        <button onClick={e => {
            // 替换 state 加 更新 state一起使用
            setCount(count + 1);
            // 遍历队列, 使用上一个 state 的返回值作为输入
            setCount(n => n + 3);
        }}>点我加4
        </button>
        <br/>
        <span style={{'color': 'red'}}>{count}</span>
        <button onClick={e => {
            // 先更新再替换
            setCount(count + 1);
            setCount(n => n + 1);
            setCount(4);
        }}>一直为4
        </button>
    </>)
}

function sendMessage(message) {
    alert(message)
}
