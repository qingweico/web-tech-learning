// web 性能指标  加载时间（LCP）、交互性（FID）和视觉稳定性（CLS） 第一渲染内容（FCP） 第一字节到达时间（TTFB） 输入延迟（INP）
import {onCLS, onFCP, onFID, onINP, onLCP, onTTFB} from "web-vitals";

// 性能指标评分阈值
const thresholds = {
    CLS: {good: 0.1, needsImprovement: 0.25},
    FID: {good: 100, needsImprovement: 300},
    LCP: {good: 2500, needsImprovement: 4000},
    FCP: {good: 1800, needsImprovement: 3000},
    INP: {good: 200, needsImprovement: 500},
    TTFB: {good: 800, needsImprovement: 1800},
};

// 性能指标处理函数
const handleMetric = (metric) => {
    const threshold = thresholds[metric.name];
    if (!threshold) return;

    const rating = metric.rating || (metric.value <= threshold.good ? "good" : metric.value <= threshold.needsImprovement ? "needs-improvement" : "poor");

    if (rating !== "good") {
        console.warn(`性能指标 ${metric.name} 需要优化:\n`, `当前值: ${metric.value.toFixed(2)}\n`, `评分: ${rating}\n`, `优: <= ${threshold.good}\n`, `中: <= ${threshold.needsImprovement}\n`, metric);
    }
};

// 监听各项性能指标
onCLS(handleMetric);
onFID(handleMetric);
onLCP(handleMetric);
onFCP(handleMetric);
onINP(handleMetric);
onTTFB(handleMetric);
