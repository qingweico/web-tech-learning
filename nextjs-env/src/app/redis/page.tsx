'use client';

import { useEffect, useState } from 'react';
import Link from "next/link";

interface DataResponse {
    value: string | null;
}

export default function RedisPage() {
    const [data, setData] = useState<string | null>(null);

    useEffect(() => {
        async function fetchData() {
            const res = await fetch('/api/redis');
            const result: DataResponse = await res.json();
            setData(result.value);
        }

        fetchData().then(r => {});
    }, []);

    return (
        <div>
            <h1>Data from Redis: {data}</h1>
            <Link href='/redis/set'>Set Redis Data</Link>
        </div>
    );
}



