import LoadingIcon from './LoadingIcon';
import styles from './LoadingMask.module.css';

export default function LoadingMask() {
    return (
        <div className={styles.mask}>
            <LoadingIcon className={styles.icon}/>
        </div>
    );
}
