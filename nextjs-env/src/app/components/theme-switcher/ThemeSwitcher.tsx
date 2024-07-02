'use client';

import {useEffect} from 'react';
import clsx from 'clsx';
import {useAppDispatch, useAppSelector} from '@/app/hooks';
import {changeTheme, initializeTheme, selectTheme} from '@/app/lib/settings/slice';
import LoadingIcon from '@/app/components/loading/LoadingIcon';
import parentStyles from '@/app/components/top-bar/TopBar.module.css';
import styles from './ThemeSwitcher.module.css';

const icons: { [key: string]: string } = {
    light: 'pi pi-sun',
    gray: 'pi pi-cloud',
    dark: 'pi pi-moon',
};

let initialized = false;

export default function ThemeSwitcher() {
    const theme = useAppSelector(selectTheme);
    const dispatch = useAppDispatch();

    // TODO Move non-reactive logic from Effects into Effect Events
    useEffect(() => {
        if (!initialized) {
            if (!theme) dispatch(initializeTheme());
            initialized = true;
        }
    }, []);

    if (theme === undefined) return <LoadingIcon className={clsx(parentStyles.icon)}/>;

    return (
        <span
            className={clsx(parentStyles.icon, icons[theme], styles.button)}
            onClick={() => dispatch(changeTheme(theme))}
        />
    );
}
