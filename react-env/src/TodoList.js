export default function TodoList({items}) {
    const name = 'Gregorio Y. Zara';
    // 使用 单层大括号 读取变量, 如果是复杂对象, 则会出现双层括号
    return (
        <>
            <h1>{name} Todo Do List</h1>
            <ol style={
                {
                    fontSize: 30,
                    backgroundColor: 'white',
                    color: 'black'
                }
            }>
                {items.map(item => (
                    <li key={item.id}>{item.item}</li>
                ))}

            </ol>
        </>
    );
}
