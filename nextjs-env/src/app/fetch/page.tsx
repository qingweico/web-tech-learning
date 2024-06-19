// Next.js 扩展了原生 fetch API, 提供了可配置选项(缓存和重新验证)
async function getData() {
    const { signal } = new AbortController()
    const res = await fetch('http://localhost:5000/user/get',
        // { cache: 'no-store' }
        // // 基于时间的重新验证
        { signal, next: { revalidate: 3600 },  cache: 'force-cache' })
    // The return value is *not* serialized
    // You can return Date, Map, Set, etc.
    console.log("getData")
    if (!res.ok) {
        // This will activate the closest `error.js` Error Boundary
        throw new Error('Failed to fetch data')
    }

    return res.json()
}

export default async function Page() {
    const data = await getData()
    await getData()
    return (<div>
        <p>{data.id}</p>
        <p>{data.username}</p>
        <p>{data.address}</p>
        <p>{data.mobile}</p>
    </div>)
}
