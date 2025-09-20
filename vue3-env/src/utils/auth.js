import { sm3, sm4 } from 'sm-crypto';
import Cookies from "js-cookie";

// 生成存储 KEY, 保证唯一性(用 SM3 摘要)
const STORAGE_KEY = sm3('VUEX_STORAGE_KEY');
// 自定义秘钥(必须是 16 字节长度)
const STORAGE_SECRET = '1234567890abcdef1234567890abcdef';


// ========== 加解密方法 ==========
function doEncrypt(plaintext, algorithm, secret) {
    if (algorithm === "SM4") {
        return sm4.encrypt(plaintext, secret, { mode: 'ecb', output: 'base64' });
    }
    throw new Error("暂不支持该算法: " + algorithm);
}

function doDecrypt(ciphertext, algorithm, secret) {
    if (!ciphertext) return null;
    if (algorithm === "SM4") {
        try {
            const decrypted = sm4.decrypt(ciphertext, secret, { mode: 'ecb', output: 'string' });
            return JSON.parse(decrypted);
        } catch (e) {
            console.error("解密失败:", e);
            return null;
        }
    }
    throw new Error("暂不支持该算法: " + algorithm);
}

// ========== Storage 包装 ==========
const Storage = {
    _get() {
        const _str = window.localStorage.getItem(STORAGE_KEY) || "";
        return doDecrypt(_str, "SM4", STORAGE_SECRET) || {};
    },
    _set(val) {
        window.localStorage.setItem(
            STORAGE_KEY,
            doEncrypt(JSON.stringify(val), "SM4", STORAGE_SECRET)
        );
    },
    get(key) {
        return this._get()[key];
    },
    set(key, value) {
        const _json = Object.assign({}, this._get());
        _json[key] = value;
        this._set(_json);
    },
    remove(key) {
        const _json = Object.assign({}, this._get());
        delete _json[key];
        this._set(_json);
    },
};

// localStorage
export function getStorageByKey(TokenKey) {
    return Storage.get(TokenKey);
}
export function setStorageByKey(TokenKey, token) {
    return Storage.set(TokenKey, token);
}
export function removeStorageByKey(TokenKey) {
    return Storage.remove(TokenKey);
}

// cookies
export function getCookieByKey(TokenKey) {
    return Cookies.get(TokenKey);
}
export function setCookieByKey(TokenKey, token) {
    return Cookies.set(TokenKey, token);
}
export function removeCookieByKey(TokenKey) {
    return Cookies.remove(TokenKey);
}
