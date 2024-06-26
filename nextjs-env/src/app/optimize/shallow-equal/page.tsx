'use client'
import {useDispatch} from 'react-redux';
import {shallowEqual, useSelector} from 'react-redux';
import {userLoginAction, userLogout} from "@/app/redux/modules/login/actions";
import React, {memo} from 'react';
import {Map} from "immutable";

const user = Map({
    id: 1,
    username: 'user001'
});
const ShallowEqualPage = memo(function ShallowEqual() {
    const dispatch = useDispatch()

    function setUserStore() {
        dispatch(userLoginAction(user.set('username', Math.random())))
    }

    function clearUserStore() {
        dispatch(userLogout())
    }

    const {userInfo} = useSelector((state: any) => {
        return {
            userInfo: state.getIn(["login", "userInfo"])
        }
    }, shallowEqual)
    console.log("Re-render...")
    return (<>
        <button onClick={setUserStore}>点我设置 login store</button>
        <button onClick={clearUserStore}>点我清空 login store</button>
        <p>{userInfo !== null ? userInfo.get('id') : ''}</p>
        <p>{userInfo !== null ? userInfo.get('username') : ''}</p>
    </>)
});
export default ShallowEqualPage
