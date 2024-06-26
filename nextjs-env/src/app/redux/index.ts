import {createStore} from "redux"
import {combineReducers} from "redux-immutable"

import {reducer as login} from "./modules/login"
const store = createStore(combineReducers({
    login
}))

export default store
