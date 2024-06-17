import React from "react";

import "./ui/global.css";
// 这个是应用程序的主要布局
export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode
}) {
    return (
        <html lang="en">
        <body>{children}
            <h1 className="text-blue-500">Message</h1>
        </body>
        </html>
    )
}
