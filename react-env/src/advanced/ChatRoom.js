import {useEffect} from 'react';

export default function ChatRoom() {
    useEffect(() => {
        console.log('Effect running');
        // 依赖数组为空 组件挂载时会立即执行 connect 函数
        const connection = createConnection();
        connection.connect();
        return () => connection.disconnect();
    }, []);
    return <h1>欢迎前来聊天</h1>;
}

function createConnection() {
    return {
        connect() {
            console.log('✅ 连接中...');
        }, disconnect() {
            console.log('❌ 断开连接。');
        }
    };
}
