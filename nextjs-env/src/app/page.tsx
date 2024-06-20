// Next js 使用 "文件系统路由"(基于文件和目录结构自动生成路由的机制, 不需要手动配置每个路由, 只需根据文件和文件夹的结构来组织页面文件)
// Next.js 默认使用服务器组件
// 在组件顶部申明 'use client' 显示使用客户端组件
// 应用路由和页面路由
// 应用程序的主页面
import Link from 'next/link';
import {AppFooterWrapper} from "@/app/components/AppFooterWrapper";
import InvoiceStatus from "@/app/components/invoice/InvoiceStatus";
import FetchPage from "@/app/fetch/page";
import GetEnvVars from "@/app/components/GetEnv";
export default function Page() {
    return <GetEnvVars/>
    // return <div>
    //     <Link href="/dashboard">dashboard</Link>
    //     <br/>
    //     <Link xhref="/fetch">fetch</Link>
    // </div>
}
