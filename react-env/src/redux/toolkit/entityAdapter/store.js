import {configureStore} from '@reduxjs/toolkit';
import usersReducer from './slice';
//import thunk from 'redux-thunk';
//import logger from 'redux-logger';

const store = configureStore({
    reducer: {
        users: usersReducer,
    },
     // Redux Toolkit 中使用多个中间件
    // middleware: (getDefaultMiddleware) =>
    //     getDefaultMiddleware().concat(thunk, logger),
});

export default store;
