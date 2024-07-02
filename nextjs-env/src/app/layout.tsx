import React from 'react';
import type {Metadata} from 'next';
import {cookies, headers} from 'next/headers';
import {Inter} from 'next/font/google';
import clsx from 'clsx';
import {AppState} from '@/app/store';
import {GlobalProvider} from '@/app/GlobalProvider';
import {getThemeGlobalClassName, getThemeInstanceName} from '@/app/lib/theme/common';
import {getDefaultTheme} from '@/app/lib/theme/server';
import {calcInitialLocale, getLocaleData} from '@/app/lib/locale/server';
import 'primeicons/primeicons.css';
import './global.css';

const inter = Inter({subsets: ['latin']});

export async function generateMetadata(): Promise<Metadata> {
    const cookieStore = cookies();
    let locale = cookieStore.get('locale')?.value;
    if (!locale) locale = calcInitialLocale(headers());

    const l = await getLocaleData('/index#metadata', locale);

    return {
        title: l.title,
        description: l.description,
    };
}

export default function RootLayout({children}: { children: React.ReactNode }) {
    const cookieStore = cookies();
    const theme = cookieStore.get('theme')?.value;
    const locale = cookieStore.get('locale')?.value;
    const validTheme = theme || getDefaultTheme();
    const validLocale = locale || calcInitialLocale(headers());

    // If we want to use a real store here and get the state, we need to create a new store every time, otherwise its
    // state won't be updated (since we never change the state on the server side).
    const serverState: Partial<AppState> = {
        settings: {
            theme: theme,
            locale: validLocale,
        },
        api: {} as any,
    };

    return (
        <html lang={validLocale}>
        <head>
            <link id="theme-link" rel="stylesheet" href={`/themes/${getThemeInstanceName(validTheme)}/theme.css`}/>
        </head>
        <body className={clsx(inter.className, getThemeGlobalClassName(validTheme))} data-theme={validTheme}>
        <GlobalProvider serverState={serverState}>
            {children}
        </GlobalProvider>
        </body>
        </html>
    );
}
