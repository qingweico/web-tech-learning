const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// vue.config.js
module.exports = {
  /*
    Vue-cli3:
    Crashed when using Webpack `import()` #2463
    https://github.com/vuejs/vue-cli/issues/2463
   */
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  publicPath: process.env.NODE_ENV === 'production' ?
      './' : '/',
  /*
  pages: {
    index: {
      entry: 'src/main.js',
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    }
  },
  */
  configureWebpack: {},

  chainWebpack: (config) => {
    config.resolve.alias
        .set('@$', resolve('src'))
        .set('@api', resolve('src/api'))
        .set('@assets', resolve('src/assets'))
        .set('@comp', resolve('src/components'))
        .set('@views', resolve('src/views'))
        .set('@layout', resolve('src/layout'))
        .set('@static', resolve('src/static'))

    config.entry('main').add('babel-polyfill');
  },

 css: {
     loaderOptions: {
       less: {
         lessOptions: {
           javascriptEnabled: true,   // 必须这样写！
           // 如果你要自定义主题，也可以在这里加：
           // modifyVars: {
           //   'primary-color': '#1890ff',
           //   'link-color': '#1890ff'
           // }
         }
       }
     }
   },

  devServer: {
    port: 3001,
    proxy: {
      '/we-diary-bill': {
        target: 'http://127.0.0.1:8094', //请求本地后台项目
        ws: true,
        changeOrigin: true,
        // pathRewrite: {
        //   '^/we-diary-bill': '/' //默认所有请求前缀，需要去掉
        // }
      },
      '/static/images': {
        target: 'http://127.0.0.1:8094', //请求本地后台项目
        ws: true,
        changeOrigin: true
      }
    },
    public: '127.0.0.1:3001', // 本地的ip:端口号
  },
  lintOnSave: false
}
