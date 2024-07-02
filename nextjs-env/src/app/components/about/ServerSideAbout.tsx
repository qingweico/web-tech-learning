import {getLocaleData} from '@/app/lib/locale/server';
import styles from './About.module.css';

export default async function ServerSideAbout({locale}: { locale: string }) {
    const l = await getLocaleData('components/about#ServerSideAbout', locale);

    return (
        <div className={styles.wrapper}>
            <div className={styles.header} dangerouslySetInnerHTML={{__html: l.header}}/>
            <div className={styles.desc} dangerouslySetInnerHTML={{__html: l.desc}}/>
            <div className={styles.comment} dangerouslySetInnerHTML={{__html: l.comment}}/>
        </div>
    );
}
