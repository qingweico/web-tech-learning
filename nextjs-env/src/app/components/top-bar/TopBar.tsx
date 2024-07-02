import React from 'react';
import styles from './TopBar.module.css';

export default function TopBar({children}: { children: React.ReactNode }) {
    return <div className={styles.wrapper}>{children}</div>;
}
