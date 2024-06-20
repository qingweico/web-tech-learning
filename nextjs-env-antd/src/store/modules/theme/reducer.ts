import { ActionExternal } from "@/common/interface/action";
import { Map } from "immutable";
import { ActionTypes } from "./constants";

const initialState = Map({
  theme: 'light'
})

function reducer(state = initialState, action: ActionExternal) {
  switch (action.type) {
    case ActionTypes.CHANGE_THEME:
      return state.set("theme", state.get("theme") === 'light' ? 'dark' : 'light')
  }
}

export {
  reducer
}
