'use client';

import clsx from 'clsx';
import {useAppSelector} from '@/app/hooks';
import {LocaleLeafObject} from '@/app/lib/locale/client';
import {useLocaleData} from '@/app/lib/locale/slice';
import {selectLocale} from '@/app/lib/settings/slice';
import LoadingBlock from '@/app/components/loading/LoadingBlock';
import ErrorBlock from '@/app/components/error/ErrorBlock';
import styles from './About.module.css';

export default function ClientSideAbout() {
    const locale = useAppSelector(selectLocale);

    const result = useLocaleData('/components/about#ClientSideAbout', locale);
    if (result.error) return <ErrorBlock error={result.error} className={clsx(styles.wrapper, styles.placeholder)}/>;
    if (!result.data) return <LoadingBlock className={clsx(styles.wrapper, styles.placeholder)}/>;

    const l = result.data as LocaleLeafObject;

    return (
        <div className={styles.wrapper}>
            <div className={styles.header} dangerouslySetInnerHTML={{__html: l.header}}/>
            <div className={styles.desc} dangerouslySetInnerHTML={{__html: l.desc}}/>
            <div className={styles.comment} dangerouslySetInnerHTML={{__html: l.comment}}/>
        </div>
    );
}
