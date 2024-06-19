import { ActionExternal } from "@/common/interface/action";
import { Map } from "immutable";
import { ActionTypes } from "./constants";

const initialState = Map({
  userInfo: null
})

function reducer(state = initialState, action: ActionExternal) {
  switch (action.type) {
    case ActionTypes.USER_LOGIN:
      return state.set("userInfo", action.data);
    case ActionTypes.EXIT:
      return state.set("userInfo", null)
    default:
      return state;
  }
}

export {
  reducer
}
