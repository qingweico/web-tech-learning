'use client'
export default function RedirectPage() {

    async function fetchData() {
        setTimeout(async () => {
            await fetch('/api/redirect');
        }, 2000)
    }
    fetchData().then(r => {})

    return (
        <div>
            <button>2s后重定向到 Dashboard</button>
        </div>
    );
}
