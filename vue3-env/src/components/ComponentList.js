import components from './index';

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


export {componentList, componentMap};
