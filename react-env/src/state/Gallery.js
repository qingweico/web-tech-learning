import {sculptureList} from '../data/data.js';
import {useState} from 'react';
// 为什么使用 state
// 局部变量的生命周期和组件息息相关
// 更改局部变量则不会触发组件的重新渲染
// state 隔离且私有 对比 ThreadLocal
// state 完全私有于声明它的组件, 父组件也无法更改
export default function Gallery() {
    const [index, setIndex] = useState(0);
    const [showMore, setShowMore] = useState(false);
    const size = sculptureList.length
    const hasPrev = index > 0
    const hasNext = index < size - 1

    function handlePrevClick() {
        if (hasPrev) {
            setIndex(index - 1);
        }
    }

    function handleNextClick() {
        if (hasNext) {
            setIndex(index + 1);
        }
    }

    function handleMoreClick() {
        setShowMore(!showMore);
    }

    let sculpture = sculptureList[index];
    return (<>
        <button
            onClick={handlePrevClick}
            disabled={!hasPrev}
        >
            Previous
        </button>
        <button onClick={handleNextClick} disabled={!hasNext}>
            Next
        </button>
        <h2>
            <i>{sculpture.name} </i>
            by {sculpture.artist}
        </h2>
        <h3>
            ({index + 1} of {sculptureList.length})
        </h3>
        <button onClick={handleMoreClick}>
            {showMore ? 'Hide' : 'Show'} details
        </button>
        {showMore && <p>{sculpture.description}</p>}
        <img
            src={sculpture.url}
            alt={sculpture.alt}
        />
    </>);
}

