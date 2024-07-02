import {cookies, headers} from 'next/headers';
import {calcInitialLocale} from '@/app/lib/locale/server';
import TopBar from '@/app/components/top-bar/TopBar';
import ServerSideAbout from '@/app/components/about/ServerSideAbout';
import ClientSideAbout from '@/app/components/about/ClientSideAbout';
import ThemeSwitcher from '@/app/components/theme-switcher/ThemeSwitcher';
import LocaleSwitcher from '@/app/components/locale-switcher/LocaleSwitcher';
import LoginPage from "@/app/components/login/LoginPage";

export default function Page() {
    const cookieStore = cookies();
    let locale = cookieStore.get('locale')?.value;
    if (!locale) locale = calcInitialLocale(headers());

    return (
        <>
            <TopBar>
                <LocaleSwitcher/>
                <ThemeSwitcher/>
            </TopBar>
            <ServerSideAbout locale={locale}/>
            <ClientSideAbout/>
            <LoginPage/>
        </>
    );
}
