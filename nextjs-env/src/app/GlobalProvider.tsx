'use client';

import React from 'react';
import {Provider} from 'react-redux';
import {PrimeReactProvider} from 'primereact/api';
import {getOrCreateStore, AppState} from '@/app/store';
import {getThemeConfiguration} from '@/app/lib/theme/common';

export function GlobalProvider({children, serverState}: {
    children: React.ReactNode;
    serverState: Partial<AppState>;
}) {
    // On the server side, this store is also created, but it's of no use to the server.
    const clientStore = getOrCreateStore(serverState);

    return (
        <Provider store={clientStore} serverState={serverState}>
            <PrimeReactProvider value={getThemeConfiguration(serverState.settings?.theme)}>
                {children}
            </PrimeReactProvider>
        </Provider>
    );
}
