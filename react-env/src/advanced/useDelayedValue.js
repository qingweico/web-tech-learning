import {useState, useEffect} from 'react';

export default function useDelayedValue(value, delay) {
    const [delayedValue, setDelayedValue] = useState(value);
    useEffect(() => {
        setTimeout(() => {
            setDelayedValue(value);
        }, delay)
    }, [delay, value]);
    return delayedValue
}
