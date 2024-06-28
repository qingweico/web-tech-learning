'use client'
import Link from 'next/link';
import {useRouter} from "next/router";

export default function LangPage() {
    const router = useRouter();
    const { locale } = router;

    return (
        <div>
            <h1>{locale === 'zh' ? '首页' : 'Home Page'}</h1>
            <p>{locale === 'zh' ? '欢迎来到首页！' : 'Welcome to the home page!'}</p>
            <div>
                <Link href="/" locale="en">
                    <a>English</a>
                </Link>
                <Link href="/" locale="zh">
                    <a>中文</a>
                </Link>
            </div>
        </div>
    );
}
