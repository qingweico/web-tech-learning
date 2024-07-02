import clsx from 'clsx';
import LoadingIcon from './LoadingIcon';
import styles from './LoadingBlock.module.css';

export default function LoadingBlock({className}: { className?: string }) {
    return (
        <div className={clsx(styles.wrapper, className)}>
            <LoadingIcon className={styles.icon}/>
        </div>
    );
}
