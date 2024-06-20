import { getCurrentTheme } from "@/utils/theme"
import styled from "styled-components"
// props 是传递给组件的属性对象, 通过 props.theme 可以接受到 父组件向其传递的 theme 属性
export const AppFooterWrapper = styled.div`
  width: 100%;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #f8f8f8;
  background-color: ${(props: any) => getCurrentTheme(props.theme)};
  transition: background-color .2s;
`
