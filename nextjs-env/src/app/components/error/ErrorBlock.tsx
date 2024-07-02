import {useState} from 'react';
import {FetchBaseQueryError} from '@reduxjs/toolkit/query';
import {SerializedError} from '@reduxjs/toolkit';
import clsx from 'clsx';
import {Button} from 'primereact/button';
import {Dialog} from 'primereact/dialog';
import styles from './ErrorBlock.module.css';

export default function ErrorBlock({blockName, error, className}: {
    blockName?: string;
    error: FetchBaseQueryError | SerializedError;
    className?: string;
}) {
    const [dialogVisible, setDialogVisible] = useState<boolean>(false);

    const errorType = 'status' in error ? 'FetchBaseQueryError' : 'SerializedError';
    console.error(`${errorType}: ${JSON.stringify(error)}`);

    return (
        <div className={clsx(styles.wrapper, className)}>
            <Button icon="pi pi-times" rounded severity="danger" aria-label="Show error"
                    className={styles.icon}
                    onClick={() => setDialogVisible(true)}
            />
            <Dialog
                className={styles.dialog}
                header={`Error occurred when rendering ${blockName ?? 'component'}`}
                visible={dialogVisible}
                onHide={() => setDialogVisible(false)}
            >
                <div key='type'>{`type: ${errorType}`}</div>
                {Object.entries(error).map(([key, value]) => {
                    if (key === 'data' && typeof value !== 'string') value = JSON.stringify(value);
                    return <div key={key}>{`${key}: ${value}`}</div>;
                })}
            </Dialog>
        </div>
    );
}
