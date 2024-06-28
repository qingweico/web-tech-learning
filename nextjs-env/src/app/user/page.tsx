'use client'
import {useEffect} from "react";

export default function UserPage() {

    useEffect(() => {
        async function fetchData() {
            await fetch('/api/user');
        }

        fetchData().then(r => {
        });
    }, []);

    return (
        <div>
            <h1>User Page</h1>
        </div>
    );
}

