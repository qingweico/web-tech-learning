import {useEffect} from 'react';

export function useKeyBoardEventHook(keyboards: Array<string> | string, handler: Function) {
    // 组合键
    useEffect(() => {
        const keyBoardEventHandler = (event: KeyboardEvent) => {
            const _isCompose = Array.isArray(keyboards);
            // 判断是Ctrl 还是 Alt
            const _isAlt = _isCompose && keyboards.includes("Alt");
            const _isControl = _isCompose && keyboards.includes("Control");

            if (_isAlt) {
                // 是否符合条件 (mac结合Alt事件会有问题)
                event.altKey && keyboards.includes(event.key) && handler();
                return;
            }
            if (_isControl) {
                event.ctrlKey && keyboards.includes(event.key) && handler();
                return;
            }
            // 正常情况 只有一个键盘事件的清情况
            keyboards === event.key && handler();
        }

        window.addEventListener("keyup", keyBoardEventHandler)

        return () => {
            window.removeEventListener("keyup", keyBoardEventHandler);
        }
    }, [keyboards, handler])
}
