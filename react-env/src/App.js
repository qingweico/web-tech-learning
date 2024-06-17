import Game from "./advanced/Game";

export default function App() {
    return (// 组件返回的 JSX 需要包含在一个单一的根元素内
        <div>
            {/*<Profile/>*/}
            {/*<Gallery/>*/}
            {/*<TodoList items={items}/>*/}
            {/*传递多个属性 使用空格分割*/}
            {/*<User user={user} priority={priority}></User>*/}
            {/*<TeaSet/>*/}
            <Game/>
        </div>);
}
