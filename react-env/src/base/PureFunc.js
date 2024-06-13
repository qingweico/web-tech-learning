function Cup({guest}) {
    // 在组件内不要修改预先已经存在的变量
    return <h2>Tea cup for guest #{guest}</h2>;
}

export default function TeaSet() {
    return <>
        <section>
            <Cup guest = {1}></Cup>
            <Cup guest = {2}></Cup>
            <Cup guest = {3}></Cup>
        </section>
    </>
}
