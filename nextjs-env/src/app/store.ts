import {combineReducers, configureStore, Middleware, Store} from '@reduxjs/toolkit';
import settingsReducer from '@/app/lib/settings/slice';
import {apiSlice} from '@/app/lib/api/slice';
const reducer = combineReducers({
    settings: settingsReducer,
    [apiSlice.reducerPath]: apiSlice.reducer,
});

const middleware: Middleware[] = [
    apiSlice.middleware,
];

let store: Store;

const createStore = (preloadedState: Partial<AppState>) =>
    configureStore({
        reducer,
        middleware: (getDefault) => getDefault().concat(middleware),
        preloadedState,
    });

/**
 * For the client, it only needs to receive the server-side state when the page is refreshed, and after that it is all
 * self-maintained.
 * If you receive the server-side state every time, since the server-side state is not updated, it will cause the state
 * to be reset after every page refresh.
 * @param preloadedState This state is only used on the first display after refreshing the page.
 */
export const getOrCreateStore = (preloadedState: Partial<AppState>) => {
    if (store) return store;
    store = createStore(preloadedState);
    return store;
}

// ReturnType(TypeScript 中的一种类型定义) 意思是获取 reducer 函数的返回类型 并将其定义为 AppState 类型
export type AppState = ReturnType<typeof reducer>;
export type AppDispatch = ReturnType<typeof createStore>['dispatch'];
