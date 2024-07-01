import { compose } from 'redux';

const add5 = (num) => num + 5;
const multiplyBy2 = (num) => num * 2;
const subtract10 = (num) => num - 10;

// 使用 compose 组合这些函数
const composedFunction = compose(subtract10, multiplyBy2, add5);

// 调用组合后的函数 (从右向左执行 结果为20)
const result = composedFunction(10);

export default result
