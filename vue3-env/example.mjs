// Node.js 默认将 .js 文件视为 CommonJS 模块. 要使用 ES模块
// 所以要么在package.json 中配置 "type": "module", 要么修改后缀名为.mjs
// 此文件运行在 Node.js 服务器上
import { createSSRApp } from 'vue'
// Vue 的服务端渲染 API 位于 `vue/server-renderer` 路径下
import { renderToString } from 'vue/server-renderer'

const app = createSSRApp({
    data: () => ({ count: 1 }),
    template: `<button @click="count++">{{ count }}</button>`
})

renderToString(app).then((html) => {
    console.log(html)
})
