import {createAction, createReducer, createSlice} from '@reduxjs/toolkit';
const initialState = {
    count: 0,
};
// 创建一个 action creator
const mul2 = createAction('mul2');


const counterSlice = createSlice({
    name: 'counter',
    initialState,
    reducers: {
        [mul2]: (state) => {
            state.count *= 2;
        },
        increment: (state) => {
            state.count += 1;
        },
        decrement: (state) => {
            state.count -= 1;
        },
        incrementByAmount: (state, action) => {
            state.count += action.payload
        },
    },
});

// Redux Toolkit 提供的 createReducer 的新写法
export const reducer = createReducer(initialState, (builder) => {
    builder
        .addCase('increment', (state) => {
            state.count += 1;
        })
        .addCase('decrement', (state) => {
            state.count -= 1;
        })
        .addCase('incrementByAmount', (state, action) => {
            state.count += action.payload;
        });
});
export const { increment, decrement, incrementByAmount} = counterSlice.actions;
    export default counterSlice.reducer;
export const incrementAsync = (amount) => (dispatch) => {
    setTimeout(() => {
        dispatch(incrementByAmount(amount))
    }, 1000)
}

