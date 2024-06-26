'use client'
import React, { useState, useEffect } from 'react';

const LazyComponent: React.FC = () => {
    const [showContent, setShowContent] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => {
            setShowContent(true);
        }, 2000);

        return () => clearTimeout(timer);
    }, []);

    return (
        <div>
            {showContent ? (
                <div>The content has been loaded!</div>
            ) : (
                <div>Loading component content...</div>
            )}
        </div>
    );
};

export default LazyComponent;
