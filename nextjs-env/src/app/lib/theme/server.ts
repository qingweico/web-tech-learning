import 'server-only';
import {themes} from '@/app/lib/theme/common';

export * from '@/app/lib/theme/common';

/**
 * Since the theme can only be initialized on the client side, the server side only provides a default value before
 * initialization.
 */
export function getDefaultTheme(): string {
    return themes[0];
}
