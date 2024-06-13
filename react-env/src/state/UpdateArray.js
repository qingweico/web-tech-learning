// 更新 state 中的数组
import {useState} from 'react';
import {hasSelectionSupport} from "@testing-library/user-event/dist/utils";

let nextId = 0;

function AppendEl() {
    const [value, setValue] = useState('');
    const [list, setList] = useState([]);

    function handleAdd() {
        setList([...list, {
            id: nextId++, value
        }])
    }

    return (<>
        <input value={value}
               onChange={(e) => setValue(e.target.value)}/>
        <button onClick={handleAdd}
                disabled={value.trim().length === 0}>添加
        </button>
        <ul>
            {list.map(e => <li key={e.id}>{e.value}</li>)}
        </ul>
    </>)
}

const element = [{
    id: 0, value: 'Marta Colvin Andrade'
}, {
    id: 1, value: 'Lamidi Olonade Fakeye'
}, {
    id: 2, value: 'Louise Nevelson'
}]

function DelEl() {
    const [list, setList] = useState(element);


    return (<>
        <ul>
            {list.map(e => <li key={e.id}>{e.value}{' '}
                <button onClick={() => {
                    setList(list.filter(el => el.id !== e.id))
                }}>删除
                </button>
            </li>)}
        </ul>
    </>)

}

let initialShapes = [{id: 0, type: 'circle', x: 50, y: 100}, {id: 1, type: 'square', x: 150, y: 100}, {
    id: 2, type: 'circle', x: 250, y: 100
},];

function ConversionArray() {
    // 改变数组中某个元素或者全部元素
    const [shapes, setShapes] = useState(initialShapes);

    function handleClick() {
        const nextShapes = shapes.map(e => {
            if (e.type === 'square') {
                return e;
            } else {
                return {
                    ...e, y: e.y + 50
                }
            }
        });
        setShapes(nextShapes)
    }

    return (<>
        <button onClick={handleClick}>所有圆形向下移动</button>
        {shapes.map(element => <div key={element.id}
                                    style={{
                                        background: 'purple',
                                        position: 'absolute',
                                        left: element.x,
                                        top: element.y,
                                        borderRadius: element.type === 'circle' ? '50%' : '',
                                        width: 20,
                                        height: 20,
                                    }}></div>)}
    </>)
}

let initialCounters = [0, 0, 0];

function ReplaceEl() {
    const [counters, setCounters] = useState(initialCounters);

    function handleIncrementClick(curIndex) {
        const nextCounters = counters.map((e, index) => {
            if (index === curIndex) {
                return e + 1
            } else {
                return e;
            }
        })
        setCounters(nextCounters);
    }

    return (<ul>
        {counters.map((e, index) => <li key={index}>{e}
            <button onClick={() => {
                handleIncrementClick(index);
            }}>加一
            </button>
        </li>)}
    </ul>)
}

let nextInsertId = 3;

function InsertEl() {
    const [value, setValue] = useState('');
    const [list, setList] = useState(element);

    function handleClick() {
        let index = 2
        const cur = [...list.slice(0, index), {id: nextInsertId++, value}, ...list.slice(index)]
        setList(cur);
    }

    return (<>
        <input value={value} onChange={e => setValue(e.target.value)}/>
        <button onClick={handleClick}
                disabled={value.trim().length === 0}>
            插入
        </button>
        <ul>
            {list.map((e) => <li key={e.id}>{e.value}</li>)}
        </ul>

    </>)
}

// 避免两个数组共享数据
let data = [{id: '0', value: '0'}, {id: '1', value: '1'}, {id: '2', value: '2'}]

function ArrayShare() {
    const [one, setOne] = useState(data)
    const [ano, setAno] = useState(data)


    // BUG 避免直接修改通过...复制过来的数组
    function handleValueChangeOld(id, value) {
        const another = [...ano];
        const item = another.find(e => e.id === id);
        item.value = value
        setAno(another);
    }

    function handleValueChange(id, value) {
        setAno(ano.map(e => {
            if (e.id === id) {
                return {
                    ...e, value
                }
            } else {
                return e;
            }
        }))
    }

    return (<>
        {one.map(e => <div key={e.id}>
            {e.value}
        </div>)}
        <hr/>
        <br/>
        {ano.map(e => <div key={e.id}>
            <input value={e.value} onChange={(el) => {
                handleValueChange(e.id, el.target.value)
            }}/></div>)}
    </>)
}

export {AppendEl, DelEl, ConversionArray, ReplaceEl, InsertEl, ArrayShare}
