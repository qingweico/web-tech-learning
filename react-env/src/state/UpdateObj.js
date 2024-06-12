// 更新 state 中的对象
// 当 state 中的值为对象时, 不要直接修改其value
// 永远将 state 看着是只读的
// 当 state 变量为对象时, 不要忘记显示的复制其他字段
import {useState} from 'react';

export default function MovingDot() {
    const [position, setPosition] = useState({
        x: 0,
        y: 0
    });
    return (
        <div
            onPointerMove={e => {
                setPosition({
                    x: e.clientX,
                    y: e.clientY
                })
            }}
            style={{
                position: 'relative',
                width: '100vw',
                height: '100vh',
            }}>
            <div style={{
                position: 'absolute',
                backgroundColor: 'red',
                borderRadius: '50%',
                transform: `translate(${position.x}px, ${position.y}px)`,
                left: -10,
                top: -10,
                width: 20,
                height: 20,
            }}/>
        </div>
    );
}

function Form() {
    const [user, setUser] = useState({
        name: 'Barbara',
        company: '',
        address: {
            region: 'Greater London',
            road: 'High Street',
            street: 'Victoria Street'
        }
    });

    function handleNameInputChange(e) {
        setUser({
            // 使用展开运算符(浅拷贝) 复制对象, 不需要单独一个一个复制
            // 只需要给改变字段赋值
            ...user,
            name: e.target.value
        });
    }

    function handleCompanyInputChange(e) {
        setUser({
            ...user,
                company: e.target.value
        });
    }
    function handleRegionInputChange(e) {
        setUser({
            ...user,
            address: {
                ...user.address,
                region: e.target.value
            }
        });
    }

    return (
        <>
            <label>
                Name:
                <input
                    value={user.name}
                    onChange={handleNameInputChange}
                />
            </label>
            <br/>
            <label>
                Company:
                <input
                    value={user.company}
                    onChange={handleCompanyInputChange}
                />
            </label>
            <br/>
            <label>
                Region:
                <input
                    value={user.address.region}
                    onChange={handleRegionInputChange}
                />
            </label>
            <p>
                {user.name}{' '}
                {user.company}{' '}
                <br/>
                {user.address.region}{' '}
                {user.address.road}{' '}
                ({user.address.street}{' '})
            </p>
        </>
    );
}

export {Form}
