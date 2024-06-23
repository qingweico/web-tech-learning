module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint',
    sourceType: 'module'
  },
  env: {
    browser: true,
    node: true,
    es6: true
  },
  extends: ['plugin:vue/recommended', 'eslint:recommended'],

  rules: {
      'vue/no-unused-components': 'warn',
      'vue/multi-word-component-names': 'off',
  }
}
