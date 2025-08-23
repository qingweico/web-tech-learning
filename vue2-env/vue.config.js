const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = {
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: process.env.VUE_APP_BASE_API,
        changeOrigin: true,
        secure: false,
        pathRewrite: {
          '^/api': ''
        },
        onProxyReq: (proxyReq, req) => {
          req._proxyStartTime = Date.now();
          const source = `http://localhost:3000${req.url}`;
          const target = `${process.env.VUE_APP_BASE_API}${proxyReq.path}`;
          console.log('\x1b[36m%s\x1b[0m', `🔄 [Proxy] ${req.method} ${source} -> ${target}`);
        },
        onProxyRes: (proxyRes, req) => {
          const responseTime = Date.now() - req._proxyStartTime;
          const source = `http://localhost:3000${req.url}`;
          const target = `${process.env.VUE_APP_BASE_API}${proxyRes.req.path}`;
          const contentLength = proxyRes.headers['content-length'] || 'unknown';

          let statusColor = '\x1b[32m';
          if (proxyRes.statusCode >= 400) statusColor = '\x1b[33m';
          if (proxyRes.statusCode >= 500) statusColor = '\x1b[31m';

          console.log(`${statusColor}✅ [Response] ${target} -> ${req.method} ${source} | ${proxyRes.statusCode} | ${responseTime}ms | ${contentLength}B\x1b[0m`);
        },
        onError: (err, req) => {
          const source = `http://localhost:3000${req.url}`;
          const target = `${process.env.VUE_APP_BASE_API}${req.url.replace('/user', '/user')}`;
          console.log('\x1b[31m%s\x1b[0m', `❌ [Proxy Error] ${req.method} ${source} -> ${target} | ${err.message}`);
        }
      }
    }
  }
}
