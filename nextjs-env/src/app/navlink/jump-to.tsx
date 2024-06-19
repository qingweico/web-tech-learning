'use client'

import { useRouter } from 'next/navigation'

export default function JumpToDashboard() {
    const router = useRouter()

    return (
        <button type="button" onClick={() => router.push('/dashboard')}>
            跳转到 Dashboard
        </button>
    )
}
