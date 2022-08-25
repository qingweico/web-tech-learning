let userId = Math.random().toString(36).slice(-6);
let serverWSUrl = "ws://localhost:8080/websocket/" + userId;
let serverWS = null;

function serverWSCreate() {
    function create() {
        try {
            console.log('创建与服务端的连接')
            serverWS = new WebSocket(serverWSUrl)
            initEventHandle()
        } catch (error) {
            console.log('创建与服务端的连接代码异常', error)
        }
    }

    let initEventHandle = () => {
        console.log('与服务端的ws事件绑定')
        serverWS.onopen = () => {
            console.log('与服务端的连接open')
        }
        serverWS.onmessage = (event) => {
            console.log('与服务端的连接message')
            if (event.data === '连接成功') return
            let data = JSON.parse(event.data)
            console.log(data)
            serverWS.onclose = () => {
                console.log('与服务端的连接close')
            }
            serverWS.onerror = (error) => {
                console.log('与服务端的连接error', error)
            }
        }
    }
    create();
}
serverWSCreate();
