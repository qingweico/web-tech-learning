import React from "react";

import "./ui/global.css";
import "./ui/layout.css";


export async function generateMetadata() {
    return {
        title: {
            default: 'My Next.js App',
            template: '%s | My Next.js App',
        },
        description: 'This is the description for the entire app',
    };
}


// 这个是应用程序的主要布局
export default function RootLayout({
                                       children,
                                       page1,
                                       page2,
                                   }: {
    children: React.ReactNode,
    page1: React.ReactNode
    page2: React.ReactNode
}) {

    return (
        <html lang="en">
        <body>
        <div className="layout-container">
            {children}
            {page1}
            {page2}
        </div>
        </body>
        </html>
    )
}
