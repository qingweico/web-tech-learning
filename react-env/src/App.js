// 使用 Provider 为了让 Redux store 能够被整个 React 应用程序访问
import "primereact/resources/themes/lara-light-cyan/theme.css";
import 'primeicons/primeicons.css';
import PrimeReactApp from "./primereact/PrimeReact";
import {PrimeReactProvider} from "primereact/api";
export default function App() {
    return (// 组件返回的 JSX 需要包含在一个单一的根元素内
        <div>
            {/*<Profile/>*/}
            {/*<Gallery/>*/}
            {/*<TodoList items={items}/>*/}
            {/*传递多个属性 使用空格分割*/}
            {/*<User user={user} priority={priority}></User>*/}
            {/*<TeaSet/>*/}
            {/*<Provider store={store}>*/}
            {/*    <ToolkitReduxCounter />*/}
            {/*</Provider>*/}
            <PrimeReactProvider>
                <PrimeReactApp />
            </PrimeReactProvider>
        </div>);
}
