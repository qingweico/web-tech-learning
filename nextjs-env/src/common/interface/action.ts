import type { Action } from "redux"

export interface ActionExternal extends Action {
  data?: any
}
