'use client'
export default function RedirectPage() {

    async function fetchData() {
        setTimeout(async () => {
            await fetch('/api/redirect');
        }, 2000)
    }

    return (
        <div>
            <button onClick={fetchData}>点我2s后重定向到 Dashboard</button>
        </div>
    );
}
