'use client'
import React, { useState } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import './LoginPage.css';
import {useAppSelector} from "@/app/hooks";
import {selectLocale} from "@/app/lib/settings/slice";
import {useLocaleData} from "@/app/lib/locale/slice";
import ErrorBlock from "@/app/components/error/ErrorBlock";
import clsx from "clsx";
import styles from "@/app/components/about/About.module.css";
import LoadingBlock from "@/app/components/loading/LoadingBlock";
import {LocaleLeafObject} from "@/app/lib/locale/common";

const LoginPage = () => {


    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = () => {
        console.log('Username:', username);
        console.log('Password:', password);
    };
    const locale = useAppSelector(selectLocale);

    const result = useLocaleData('/components/login#Login', locale);
    if (result.error) return <ErrorBlock error={result.error} className={clsx(styles.wrapper, styles.placeholder)}/>;
    if (!result.data) return <LoadingBlock className={clsx(styles.wrapper, styles.placeholder)}/>;

    const l = result.data as LocaleLeafObject;
    return (
        <div className="login-container">
            <h2>{l.title}</h2>
            <div className="p-fluid">
                <div className="p-field">
                    <label htmlFor="username">{l.username}</label>
                    <InputText id="username" value={username} onChange={(e) => setUsername(e.target.value)} />
                </div>
                <div className="p-field">
                    <label htmlFor="password">{l.password}</label>
                    <InputText id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <div className="p-field">
                    <Button onClick={handleLogin} label={l.submit}></Button>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
