//  Jest 默认的测试文件命名规则 (.test.ts 或者 .spec.js 结尾)
import {describe, expect, test} from '@jest/globals';

const sum = (a : number, b : number) => {
    return a + b;
}
// 简单测试 test (别名 : it)
// test(name, fn, timeout)


test('adds 1 + 2 to equal 3', () => {
    expect(sum(1, 2)).toBe(3);
}, 1);


// describe 是将多个相关的测试组合在一起

const game = {
    start: true,
    end: false,
};

describe('game', () => {
    test('game start', () => {
        expect(game.start).toBeTruthy();
    });

    test('game end', () => {
        expect(game.end).toBeFalsy();
    });
});

// 标记某个测试用例预期会失败
test.failing('it is not equal', () => {
    expect(5).toBe(6);
});
