/**
 * For a detailed explanation regarding each configuration property, visit:
 * https://jestjs.io/docs/configuration
 * https://kulshekhar.github.io/ts-jest/docs/getting-started/options/
 */

/** @type {import('ts-jest').JestConfigWithTsJest} */
const config = {
    preset: 'ts-jest',
    // @see https://stackoverflow.com/questions/42260218/jest-setup-syntaxerror-unexpected-token-export
    moduleNameMapper: {
        '^@/(.*)$': '<rootDir>/src/$1',
        '^@resources/(.*)$': '<rootDir>/resources/$1',
        '^lodash-es(/.+)?$': 'lodash$1',
    },
};

export default config;
