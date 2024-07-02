'use client';

import {locale as setPrimeReactLocale, addLocale as addPrimeReactLocale} from 'primereact/api';
import primeReactDataZhCn from '@/resources/locales/primereact.zh-CN.json';

export * from '@/app/lib/locale/common';

export function onLocaleInitialize(locale: string): void {
    addPrimeReactLocale('zh-CN', primeReactDataZhCn['zh-CN']);
    setPrimeReactLocale(locale);
}

export function onLocaleUpdate(locale: string): void {
    setPrimeReactLocale(locale);
}
