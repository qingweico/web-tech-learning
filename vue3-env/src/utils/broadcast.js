import mitt from "mitt";

const emitter = mitt();
// 基于 mitt 库实现的发布-订阅模式, 用于单个页面内的组件间通信
const Broadcast = {
    list: [],
    on(str, fn) {
        emitter.on(str, fn);
    },
    off(str, fn) {
        emitter.off(str, fn);
    },
    emit(str, obj) {
        emitter.emit(str, obj);
    },
};
// 浏览器多tab级别的事件广播监听
const ChannelBroadCast = {
    callback: [],
    pageChannel: null,
    _sendEvent(name, data) {
        this.callback.forEach((one) => {
            if (one.name === name) {
                one.callback(data);
            }
        });
    },
    init() {
        if (!this.pageChannel) {
            try {
                this.pageChannel = new BroadcastChannel("Browser-Broadcast");
                this.pageChannel.onmessage = (e) => {
                    const {data} = e;
                    this._sendEvent(data.name, data.data);
                };
                this.pageChannel.onmessageerror = (e) => {
                    console.error("onmessageerror：", e);
                };
            } catch (e) {
                console.log("浏览器不支持BroadcastChannel");
            }
        }
    },
    on(str, fn) {
        if (!this.pageChannel) {
            this.init();
        }
        try {
            this.callback.push({
                name: str,
                callback: fn,
            });
        } catch (e) {
        }
    },
    emit(str, obj) {
        if (!this.pageChannel) {
            this.init();
        }
        try {
            this.pageChannel.postMessage({
                name: str,
                data: obj,
            });
        } catch (e) {
        }
    },
};

export default Broadcast;

export {ChannelBroadCast};
