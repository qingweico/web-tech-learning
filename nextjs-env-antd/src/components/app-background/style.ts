// styled-component 是一个结合了 JavaScript 和 CSS、允许在
// JavaScript 文件中直接定义和管理样式的样式库, 实现组件级别的样式管理
import styled from "styled-components"
// AppBackGroundWrapper是一个包含基本定位和层叠顺序样式的 div
export const AppBackGroundWrapper = styled.div`
  top: 0;
  left: 0;
  z-index: -99;

  .theme-bg-enter {
    transform: translateY(-100%);
  }
  .theme-bg-enter-active {
    transform: translateY(0);
    transition: transform .2s;
  }
  .theme-bg-enter-done {
    transform: translateY(0);
    transition: transform .2s;
  }
  .theme-bg-exit {
    transform: translateY(0);
  }
  .theme-bg-exit-active {
    transform: translateY(-100%);
    transition: transform .2s;
  }
  .theme-bg-exit-done {
    transform: translateY(-100%);
    transition: transform .2s;
  }
`
