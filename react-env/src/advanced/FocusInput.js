import {useRef} from 'react';

export default function FocusInput() {
    const inputRef = useRef(null);

    function handleClick() {
        inputRef.current.focus();
    }

    return (<div>
            <input ref={inputRef} type='text'/>
            <button onClick={handleClick}>聚焦输入框</button>
        </div>)
}
