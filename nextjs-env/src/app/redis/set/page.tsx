'use client';

import { useState, FormEvent } from 'react';

export default function SetDataPage() {
    const [key, setKey] = useState<string>('');
    const [value, setValue] = useState<string>('');

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const res = await fetch('/api/redis', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ key, value }),
        });

        if (res.ok) {
            alert('Key set successfully');
        } else {
            alert('Failed to set key');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Key</label>
                <input type="text" value={key} onChange={(e) => setKey(e.target.value)} />
            </div>
            <div>
                <label>Value</label>
                <input type="text" value={value} onChange={(e) => setValue(e.target.value)} />
            </div>
            <button type="submit">Set Data</button>
        </form>
    );
}
