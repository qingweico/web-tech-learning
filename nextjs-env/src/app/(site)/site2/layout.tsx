'use client'
import React from "react";
export default function Layout({
                                            children,
                                        }: {
    children: React.ReactNode
}) {

    return (<>
        <h1>这个是Site2的主要布局</h1>
        <section>{children}</section>
    </>)
}
