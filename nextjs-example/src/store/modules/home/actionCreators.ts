import { ActionExternal } from "@/common/interface/action"
import { ActionTypes } from "./constants"

// 显示加载
export const hideLoadingAction = (): ActionExternal => {
  return {
    type: ActionTypes.HIDE_LOADING
  }
}
// 隐藏加载
export const showLoadingAction = (): ActionExternal => {
  return {
    type: ActionTypes.SHOW_LOADING
  }
}
