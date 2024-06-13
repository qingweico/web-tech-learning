// 响应式 Effect 的生命周期
import {useState, useEffect} from 'react';

const serverUrl = 'https://localhost:1234';

function ChatRoom({roomId}) {
    useEffect(() => {
        const connection = createConnection(serverUrl, roomId);
        connection.connect();
        return () => connection.disconnect();
    }, [roomId]);

    return <h1>欢迎来到 {roomId} 房间</h1>;
}

function createConnection(serverUrl, roomId) {
    return {
        connect() {
            console.log('✅ 连接到 "' + roomId + '" 房间，在' + serverUrl + '...');
        }, disconnect() {
            console.log('❌ 断开 "' + roomId + '" 房间，在' + serverUrl);
        }
    };
}

export default function Lifecycle() {
    const [roomId, setRoomId] = useState('general');
    return (<>
            <label>
                选择聊天室 {' '}
                <select value={roomId}
                        onChange={e => setRoomId(e.target.value)}>
                    <option value="general">所有</option>
                    <option value="travel">旅游</option>
                    <option value="music">音乐</option>
                </select>
            </label>
            <hr/>
            <ChatRoom roomId={roomId}></ChatRoom>
        </>)
}
