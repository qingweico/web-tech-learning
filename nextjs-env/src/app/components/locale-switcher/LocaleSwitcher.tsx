'use client';

import {useEffect} from 'react';
import clsx from 'clsx';
import {Dropdown} from 'primereact/dropdown';
import {useAppDispatch, useAppSelector} from '@/app/hooks';
import {changeLocale, selectLocale} from '@/app/lib/settings/slice';
import {localeNames} from '@/app/lib/locale/common';
import {onLocaleInitialize} from '@/app/lib/locale/client';
import parentStyles from '@/app/components/top-bar/TopBar.module.css';
import styles from './LocaleSwitcher.module.css';

let initialized = false;

export default function LocaleSwitcher() {
    const locale = useAppSelector(selectLocale);
    const dispatch = useAppDispatch();

    // TODO Move non-reactive logic from Effects into Effect Events
    useEffect(() => {
        if (!initialized) {
            onLocaleInitialize(locale);
            initialized = true;
        }
    }, []);

    const dropdownOptions = Object.keys(localeNames).map(
        (key) => ({key, name: localeNames[key]})
    );

    return (
        <Dropdown
            className={clsx(parentStyles.icon, styles.dropdown)}
            value={locale}
            options={dropdownOptions}
            optionLabel='name'
            optionValue='key'
            onChange={e => dispatch(changeLocale(e.value))}
        />
    );
}
