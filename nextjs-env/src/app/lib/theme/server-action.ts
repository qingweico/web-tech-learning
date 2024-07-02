'use server';

import {cookies} from 'next/headers';

/**
 * [Server Actions](https://nextjs.org/docs/app/api-reference/functions/server-actions)
 */
export async function saveTheme(theme: string) {
    cookies().set('theme', theme);
}
