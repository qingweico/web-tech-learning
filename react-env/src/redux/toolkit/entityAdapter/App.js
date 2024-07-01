import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {selectAllUsers, addUser, selectTotal} from './slice';
import ComposeFunc from  "../composeFunc";
import {nanoid} from 'nanoid'

const UsersComponent = () => {
    const dispatch = useDispatch();
    const users = useSelector(selectAllUsers);

    const total = useSelector(selectTotal);

    const handleAddUser = () => {
        dispatch(addUser({id: users.length + 1, name: nanoid()}));
    };

    return (
        <div>
            <h1>Users</h1>
            <h1>数量 : {total}</h1>
            <h1>结果 : {ComposeFunc}</h1>
            <button onClick={handleAddUser}>Add User</button>
            <ul>
                {users.map((user) => (
                    <li key={user.id}>{user.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default UsersComponent;
