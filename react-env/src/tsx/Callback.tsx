// Hook useCallback
// 在依赖项没有发生变化时返回缓存的函数引用, 从而避免不必要的重新创建函数
import React, {useState, useCallback} from 'react';

export default function CallbackApp() {
    const [value, setValue] = useState("Change me");

    const handleChange = useCallback<React.ChangeEventHandler<HTMLInputElement>>((event) => {
        setValue(event.currentTarget.value);
    }, [setValue])

    return (
        <>
            <input value={value} onChange={handleChange}/>
            <p>值: {value}</p>
        </>
    );
}
