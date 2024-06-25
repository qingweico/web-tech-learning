'use client'

import useSWR from 'swr'

const fetcher = (url: string) => fetch(url).then((r) => r.json())
interface PageProps {
    id: string;
}
export default function SwrPage({ id } : PageProps) {
    const { data, error } = useSWR(
        `https://jsonplaceholder.typicode.com/posts/${id}`,
        fetcher
    )
    if (error) return 'Failed to load'
    if (!data) return 'Loading...'

    return data.title
}
