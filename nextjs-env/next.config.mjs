/** @type {import('next').NextConfig} */
const nextConfig = {
    // output: 'export',
    // images: {
    //     loader: 'custom',
    //     loaderFile: '.src/app/components/img-loader.ts',
    // },
    async redirects() {
        return [
            // Basic redirect
            {
                source: '/index',
                destination: '/',
                permanent: true,
            },
            // Wildcard path matching
            {
                source: '/post/:id',
                destination: '/art/:id',
                permanent: true,
            },
        ]
    },
    // i18n: {
    //     // 支持的语言列表
    //     locales: ['en', 'zh'],
    //     // 默认语言
    //     defaultLocale: 'en',
    // },
    experimental: {
        serverActions: true,
    },
};
export default nextConfig;
