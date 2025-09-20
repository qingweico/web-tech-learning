import {ElLoading} from "element-plus";

/**
 * 全屏加载
 * @param {string} text 显示文字
 * @param {string} spinner 自定义加载图标类名
 * @param {string} background 背景色
 * @returns ElLoading 实例
 */
export function showFullScreenLoading(
    text = "拼命加载中",
    spinner,
    background = "rgba(0, 0, 0, 0.6)"
) {
    return ElLoading.service({
        // 锁定页面滚动(全屏遮罩适用)
        lock: true,
        text,
        spinner,
        background,
    })
}

export function isNull(arg1) {
    return !!(!arg1 && arg1 !== 0 && typeof arg1 !== "boolean");
}

export function loadScript(url) {
    return new Promise((resolve, reject) => {
        const _script = document.createElement("script");
        _script.setAttribute("src", url);
        _script.setAttribute("charset", "utf-8");
        _script.onload = resolve;
        _script.onerror = reject;
        _script.setAttribute("type", "text/javascript");
        document.head.appendChild(_script);
    });
}

export function encodeUrlParam(key, value) {
    if (typeof key === "string") {
        return key + "=" + encodeURI(value);
    } else if (typeof key === "object") {
        let str = "";
        for (const k in key) {
            if (key[k] != null) {
                str += "&" + k + "=" + encodeURI(key[k]);
            } else {
                str += "&" + k + "=";
            }
        }
        if (str.length > 0) {
            return str.substring(1);
        } else {
            return "";
        }
    }
}

export function hexToString(hex) {
    let tmp = "";
    if (hex.length % 2 === 0) {
        for (let i = 0; i < hex.length; i += 2) {
            tmp += "%" + hex.charAt(i) + hex.charAt(i + 1);
        }
    }
    return decodeURIComponent(tmp);
}

/**
 * 字符串转16进制
 * @param str
 * @returns {string}
 */
export function stringToHex(str) {
    let hex = "";
    for (let i = 0; i < str.length; i++) {
        hex += str.charCodeAt(i).toString(16);
    }
    return hex;
}

export function base64ToArrayBuffer(base64) {
    // 解码使用base64编码的字符串
    const binary_string = window.atob(base64);
    // 获取长度
    const len = binary_string.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return String.fromCharCode.apply(null, bytes);
}

export function getUUID() {
    const s = [];
    const hexDigits = "0123456789abcdef";
    for (let i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
    s[8] = s[13] = s[18] = s[23] = "-";

    const uuid = s.join("");
    return uuid.replace(/-/g, "");
}

export function getRandomString(len = 16) {
    let result = "";
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (let i = 0; i < len; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return result;
}

export function downloadFile(url, name) {
    const $a = document.createElement("a");
    $a.href = url;
    $a.name = name;
    $a.download = name;
    $a.click();
}

export function getQueryParams(name) {
    const reg = new RegExp(name + "=([^&#]*)");
    const arr = location.href.match(reg);
    if (arr) {
        return decodeURIComponent(arr[1]);
    } else {
        return "";
    }
}
