/** @type {import('next').NextConfig} */
import { createProxyMiddleware } from 'http-proxy-middleware' ;
const nextConfig = {
    async rewrites() {
        return [
            {
                source: '/api/:path*',
                destination: 'http://localhost:5000/:path*'
            }
        ];
    },
    reactStrictMode: false,
    async redirects() {
        return [
            {
                source: '/index',
                destination: '/',
                permanent: true,
            },
        ]
    },
    webpackDevMiddleware: config => {
        config.proxy = {
            '/user': {
                target: 'localhost:5000',
                changeOrigin: true,
                pathRewrite: { '^/user': '' },
            },
        };
        return config;
    },
};
export default nextConfig;
