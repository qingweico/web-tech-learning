import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

import { match } from '@formatjs/intl-localematcher'
import Negotiator from 'negotiator'

// 请求标头上的 Accept-Language 选项
let headers = { 'accept-language': 'zh-CN,zh;q=0.9' }
let languages = new Negotiator({ headers }).languages()
let locales = ['en-US', 'zh-CN', 'zh'];
let defaultLocale = 'zh-CN'

export function middleware(request: NextRequest) {
    // const { pathname } = request.nextUrl
    // const pathnameHasLocale = locales.some(
    //     (locale) => pathname.startsWith(`/${locale}/`) || pathname === `/${locale}`
    // )
    // if (pathnameHasLocale) return
    // const locale = 'zh-CN'
    // request.nextUrl.pathname = `/${locale}${pathname}`
    // return NextResponse.redirect(request.nextUrl)
    return NextResponse.redirect(new URL('/login', request.url))
}

export const config = {
    // matcher: [
    //     // Skip all internal paths (_next)
    //     '/((?!_next).*)',
    //     // Optional: only run on root (/) URL
    //     // '/'
    // ],
    matcher: '/post/:path*',
}
match(languages, locales, defaultLocale)
