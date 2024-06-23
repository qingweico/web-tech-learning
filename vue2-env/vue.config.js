const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = {
  devServer: {
    port: 3000,
    proxy: {
      '/user': {
        target: process.env.VUE_APP_BASE_API,
        changeOrigin: true,
        secure: false,
        pathRewrite: {
          '^/user': 'user'
        }
      }
    },
  },
}
