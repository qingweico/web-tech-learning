import {createContext} from "react";
// 使用 createContext 和 useContext 用于实现上下文的创建和使用, 从而在组件树中共享数据
// createContext 是 React 提供的一个函数, 用于创建一个上下文对象
// 上下文对象可以让你在组件树中跨越多层级传递数据, 而不需要通过逐层的 props
// useContext 是 React 提供的一个 Hook, 用于在函数组件中使用上下文对象
// 它接受一个上下文对象(由 createContext 创建)并返回上下文的当前值
export const ThemeContext = createContext<string>("dark");
