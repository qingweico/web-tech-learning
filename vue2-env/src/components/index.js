// 自动导入组件
const requireComponent = require.context(
    './', // 指定要扫描的目录
    true, // 是否扫描子目录
    /\.vue$/ // 匹配 .vue 文件
);

const components = {};

requireComponent.keys().forEach(filename => {
    const componentConfig = requireComponent(filename);
    const componentName = filename
        .replace(/^\.\/(.*)\.\w+$/, '$1')
        .replace(/\/index$/, '')
        .replace(/-([a-z])/g, (g) => g[1].toUpperCase())
        .replace(/^[a-z]/, (g) => g.toUpperCase());

    components[componentName] = componentConfig.default || componentConfig;
});

const componentList = Object.keys(components).map((componentName) => ({
    label: componentName,
    value: components[componentName]
}));

const componentMap = new Map(
    Object.keys(components).map((componentName) => [
        componentName,
        components[componentName]
    ])
);

export { componentList, componentMap };
