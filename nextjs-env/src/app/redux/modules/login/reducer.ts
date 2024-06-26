import {Map} from "immutable";
import {LoginActionTypes} from "./constants";
import {ActionExtend} from "./actions.js";

const initialState = Map({
    userInfo: null
})

function reducer(state = initialState, action: ActionExtend) {
    switch (action.type) {
        case LoginActionTypes.USER_LOGIN:
            return state.set("userInfo", action.data);
        case LoginActionTypes.USER_LOGOUT:
            return state.set("userInfo", null)
        default:
            return state;
    }
}

export {
    reducer
}
