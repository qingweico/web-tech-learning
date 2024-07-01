import { configureStore } from '@reduxjs/toolkit';
import userReducer from './slice';

const store = configureStore({
    reducer: {
        users: userReducer,
    },
});

export default store;
