import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { increment, decrement } from './actions';

const Counter = () => {
    // 使用useSelector从store中获取状态
    const count = useSelector((state) => state.count);
    // 使用useDispatch获取dispatch函数
    const dispatch = useDispatch();

    return (
        <div>
            <h1>Count: {count}</h1>
            <button onClick={() => dispatch(increment())}>Increment</button>
            <button onClick={() => dispatch(decrement())}>Decrement</button>
        </div>
    );
};

export default Counter;
