import { defineAsyncComponent } from 'vue';

const requireComponent = require.context(
    './', // 指定要扫描的目录
    true, // 是否扫描子目录
    /\.vue$/ // 匹配 .vue 文件
);


const components = requireComponent.keys().reduce((acc, filename) => {
    const componentName = filename
        .replace(/^\.\/(.*)\.\w+$/, '$1')
        .replace(/\/index$/, '')
        .replace(/-([a-z])/g, (g) => g[1].toUpperCase())
        .replace(/^[a-z]/, (g) => g.toUpperCase());
    acc[componentName] =  requireComponent(filename).default || requireComponent(filename);
    return acc;
}, {});

export default components;
