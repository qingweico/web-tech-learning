export default function User({user, priority = 0}) {
    const disabled = user.disabled
    // 条件渲染
    const {account, address} = user;
    const no = account.no
    const username = account.username
    return (<div>
        {disabled && <del>该用户已禁用</del>}
        <p>用户名: {username}</p>
        <p>用户账号: {no}</p>
        <p>优先级: {priority === 0 ? '正常' : '优先'}</p>
        <Address {...address} />
    </div>);
}

function Address(address) {
    const region = address.region
    const road = address.road
    const street = address.street
    const number = address.number
    return (<div>
        <p>区: {region}</p>
        <p>路: {road}</p>
        <p>街道: {street}</p>
        <p>门牌号: {number}</p>
    </div>)

}
