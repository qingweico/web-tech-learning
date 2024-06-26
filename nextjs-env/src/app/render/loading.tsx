import React, { Suspense } from 'react';
const simulateDelay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));
// 延迟加载组件
const LazyComponent =  React.lazy(() =>
    simulateDelay(2000).then(() => import('@/app/render/LazyComponent'))
);
export default function LoadingPage() {
    return (
        <Suspense fallback={<div>Loading...</div>}>
            <LazyComponent />
        </Suspense>
    );
}
