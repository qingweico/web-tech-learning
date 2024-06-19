import { Image } from 'antd';
import React, { memo } from 'react';
// 通过 memo 高阶函数包装, 以优化性能
// memo 会缓存组件的渲染结果, 只有在组件的 props 发生变化时才会重新渲染组件
export default memo(function NOT_FOUNT() {
  return (
    <Image src={require("@/assets/svg/404.svg")} alt='404'/>
  );
});
