'use client'
// Next.js 中有四种方式可以在路由之间导航
// 1 使用<Link>组件(内置组件)
// 2 使用useRouter钩子(客户端组件)
// 3 使用redirect函数(服务器组件)
// 4 使用原生History API
import Link from 'next/link';
import { useRouter } from 'next/router';
const links = [
    {name: '菜单管理', href: "/system/menu"},
    {name: '角色管理', href: "/system/role"},
    {name: '权限管理', href: "/system/prem"}
]

export default function NavLinks() {
    return (
        <>
            {links.map((link) => {
                return (
                    <Link
                        key={link.name}
                        href={link.href}
                        className="flex h-[48px] grow items-center justify-center gap-2 rounded-md bg-gray-50 p-3 text-sm font-medium hover:bg-sky-100 hover:text-blue-600 md:flex-none md:justify-start md:p-2 md:px-3"
                    >
                        <p className="hidden md:block">{link.name}</p>
                    </Link>
                )
            })}
        </>
    );
}
