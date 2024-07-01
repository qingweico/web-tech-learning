import React, {useState} from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {increment, decrement, incrementByAmount, incrementAsync} from './counterSlice';

const ToolkitReduxCounter = () => {
    const count = useSelector((state) => state.counter.count);
    const dispatch = useDispatch();
    const [incrementAmount, setIncrementAmount] = useState('2');
    return (
        <div>
            <h1>Count: {count}</h1>
            <button onClick={() => dispatch(increment())}>Increment</button>
            <button onClick={() => dispatch(decrement())}>Decrement</button>
            <hr/>
            <input
                value={incrementAmount}
                onChange={e => setIncrementAmount(e.target.value)}
            />
            <button
                onClick={() =>
                    dispatch(incrementByAmount(Number(incrementAmount) || 0))
                }
            >
                Add Amount
            </button>
            <button
                onClick={() => dispatch(incrementAsync(Number(incrementAmount) || 0))}
            >
                Add Async
            </button>
        </div>
    );
};

export default ToolkitReduxCounter;
