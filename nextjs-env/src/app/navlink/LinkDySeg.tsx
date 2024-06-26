// Link to dynamic segments
'use client'
import Link from 'next/link'
const posts = [
    {id: '1', name: '文章一', href: "/post"},
    {id: '2', name: '文章二', href: "/post"},
    {id: '3', name: '文章三', href: "/post"}
]

export default function PostList() {
    return (
        <ul>
            {posts.map((post) => (
                <li key={post.id}>
                    <Link href={`/post/${post.id}`}>{post.name}</Link>
                </li>
            ))}
        </ul>
    )
}
