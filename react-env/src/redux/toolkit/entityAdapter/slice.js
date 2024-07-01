import {createSlice, createEntityAdapter} from "@reduxjs/toolkit";

// 创建一个实体适配器
const usersAdapter = createEntityAdapter();

// 获取初始状态
const initialState = usersAdapter.getInitialState({
    status: 'idle', error: null,
});

const usersSlice = createSlice({
    name: 'users', initialState, reducers: {
        // 使用实体适配器提供的 reducer 方法
        addUser: usersAdapter.addOne,
        addUsers: usersAdapter.addMany,
        updateUser: usersAdapter.updateOne,
        removeUser: usersAdapter.removeOne,
    }, extraReducers: (builder) => {
    },
});

export const {addUser, addUsers, updateUser, removeUser} = usersSlice.actions;
export default usersSlice.reducer;
export const {
    selectAll: selectAllUsers, selectTotal
} = usersAdapter.getSelectors((state) => state.users);
