import {startTransition} from 'react';
import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {AppDispatch, AppState} from '@/app/store';
import {calcInitialTheme, getNextTheme, getThemeSetByServer} from '@/app/lib/theme/client';
import {saveTheme} from '@/app/lib/theme/server-action';
import {onLocaleUpdate} from '@/app/lib/locale/client';
import {saveLocale} from '@/app/lib/locale/server-action';

export interface SettingsState {
    theme: string | undefined;
    locale: string;
}

const getInitialState = (): SettingsState => {
    return {} as SettingsState;
}

export const settingsSlice = createSlice({
    name: 'settings',
    initialState: getInitialState,
    reducers: {
        setTheme: (state, action: PayloadAction<string>) => {
            state.theme = action.payload;
        },
        setLocale: (state, action: PayloadAction<string>) => {
            state.locale = action.payload;
        },
    },
});

const {setTheme, setLocale} = settingsSlice.actions;

export const selectTheme = (state: AppState): SettingsState['theme'] => state.settings.theme;
export const selectLocale = (state: AppState): SettingsState['locale'] => state.settings.locale;

export default settingsSlice.reducer;

export const initializeTheme = () => {
    return (dispatch: AppDispatch) => {
        const initialTheme = calcInitialTheme();
        dispatch(setTheme(initialTheme));
        if (initialTheme !== getThemeSetByServer()) {
            // https://nextjs.org/docs/app/api-reference/functions/server-actions#invocation
            startTransition(() => {
                saveTheme(initialTheme);
            });
        }
    };
};

export const changeTheme = (currTheme: string) => {
    return (dispatch: AppDispatch) => {
        const newTheme = getNextTheme(currTheme);
        dispatch(setTheme(newTheme));
        // https://nextjs.org/docs/app/api-reference/functions/server-actions#invocation
        startTransition(() => {
            saveTheme(newTheme);
        });
    };
};

export const changeLocale = (locale: string) => {
    return (dispatch: AppDispatch) => {
        dispatch(setLocale(locale));
        onLocaleUpdate(locale);
        // https://nextjs.org/docs/app/api-reference/functions/server-actions#invocation
        startTransition(() => {
            saveLocale(locale);
        });
    };
}
