import type {Action} from "redux"
import {LoginActionTypes} from "@/app/redux/modules/login/constants";

export interface ActionExtend extends Action {
    data?: any
}

export const userLoginAction = (data: any) => (
    {type: LoginActionTypes.USER_LOGIN, data})
export const userLogout = () => ({type: LoginActionTypes.USER_LOGOUT})
