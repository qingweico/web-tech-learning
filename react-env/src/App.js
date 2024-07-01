// 使用 Provider 为了让 Redux store 能够被整个 React 应用程序访问
import "primereact/resources/themes/lara-light-cyan/theme.css";
import 'primeicons/primeicons.css';
import {Provider} from "react-redux";

import {store} from "./services/store"
import React, {useState} from "react";
import PokemonApp from "./services/PokemonPage";

export default function App() {
    const [userId, setUserId] = useState(1)
    return (// 组件返回的 JSX 需要包含在一个单一的根元素内
        <div>
            {/*<Profile/>*/}
            {/*<Gallery/>*/}
            {/*<TodoList items={items}/>*/}
            {/*传递多个属性 使用空格分割*/}
            {/*<User user={user} priority={priority}></User>*/}
            {/*<TeaSet/>*/}
            <Provider store={store}>
                <PokemonApp/>
            </Provider>
            {/*<input onChange={(e) => setUserId(e.target.value)}></input>*/}
            {/*<PrimeReactProvider>*/}
            {/*    <PrimeReactApp />*/}
            {/*/!*</PrimeReactProvider>*!/*/}
            {/*<CraftApp/>*/}
        </div>);
}
