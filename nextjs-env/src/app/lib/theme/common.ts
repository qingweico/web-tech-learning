import {APIOptions} from "primereact/api";

export const themes = ['light', 'gray', 'dark'];

const instanceNames: { [key: string]: string } = {
    light: 'mdc-light-indigo',
    gray: 'mdc-light-indigo',
    dark: 'mdc-dark-indigo',
};

export function getNextTheme(theme: string) {
    let nextIdx = themes.indexOf(theme) + 1;
    if (nextIdx >= themes.length) nextIdx = 0;
    return themes[nextIdx];
}

export function getThemeInstanceName(theme: string): string {
    return instanceNames[theme];
}

export function getThemeGlobalClassName(theme: string | undefined): string {
    return 'p-input-filled';
}

export function getThemeConfiguration(theme: string | undefined): Partial<APIOptions> {
    return {
        appendTo: 'self',
        inputStyle: 'filled',
        ripple: false,
    };
}
