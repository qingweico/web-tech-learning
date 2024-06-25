'use client'
import React from "react";
import { useSelectedLayoutSegment } from 'next/navigation';
import { useSelectedLayoutSegments } from 'next/navigation';
// 这个是dashboard的主要布局
export default function DashboardLayout({
                                            children,
                                        }: {
    children: React.ReactNode
}) {
    const segment = useSelectedLayoutSegment();
    const segments = useSelectedLayoutSegments();
    return (<>
        <h1>这个是dashboard的主要布局</h1>
        <h1>Current Segment: {segment}</h1>
        <h1>Current Segments: {segments.join(' / ')}</h1>
        <section>{children}</section>
    </>)
}
