'use client';

export * from '@/app/lib/theme/common';

export function calcInitialTheme(): string {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    return mediaQuery.matches ? 'dark' : 'light';
}

export function getThemeSetByServer(): string | undefined {
    return document.body.dataset.theme;
}
