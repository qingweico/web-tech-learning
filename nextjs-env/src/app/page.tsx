'use client'
// Next js 使用 "文件系统路由"(基于文件和目录结构自动生成路由的机制, 不需要手动配置每个路由, 只需根据文件和文件夹的结构来组织页面文件)
// Next.js 默认使用服务器组件
// 在组件顶部申明 'use client' 显示使用客户端组件
// 应用路由和页面路由
// 应用程序的主页面
import Template from './template';
import {Metadata} from 'next'
import DashboardPage from "@/app/dashboard/page";
import RedisPage from "@/app/redis/page";
import PostList from "@/app/navlink/LinkDySeg";
import {PathnamePage} from "@/app/navlink/UsePathname";
import RedirectPage from "@/app/navlink/redirect";
import ShallowEqualPage from "@/app/optimize/shallow-equal/page";
import {Provider} from "react-redux";
import store from "@/app/redux";
import LoadingPage from "@/app/render/loading";
import PlayerPower from "@/app/render/ReactFc";

// export const metadata: Metadata = {
//     title: 'Next.js',
// }
export default function Page() {
    return (
        <Template>
            <DashboardPage/>
            <RedisPage></RedisPage>
            <PostList/>
            <PathnamePage></PathnamePage>
            <RedirectPage/>
            <Provider store={store}>
                <ShallowEqualPage/>
            </Provider>
            <LoadingPage></LoadingPage>
            <PlayerPower speed={2000} strength={3000}/>
        </Template>
    )
}
