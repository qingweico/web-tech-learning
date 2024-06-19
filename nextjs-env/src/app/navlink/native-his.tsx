'use client'
// 使用原生 History API
import { useSearchParams } from 'next/navigation'
import { usePathname } from 'next/navigation'
export default function SortProducts() {
    const searchParams = useSearchParams()

    function updateSorting(sortOrder: string) {
        const params = new URLSearchParams(searchParams.toString())
        params.set('sort', sortOrder)
        window.history.pushState(null, '', `?${params.toString()}`)
    }

    return (
        <>
            <button onClick={() => updateSorting('asc')}>Sort Ascending</button>
            <button onClick={() => updateSorting('desc')}>Sort Descending</button>
        </>
    )
}

function LocaleSwitcher() {
    const pathname = usePathname()

    function switchLocale(locale: string) {
        const newPath = `/${locale}${pathname}`
        window.history.replaceState(null, '', newPath)
    }

    return (
        <>
            <button onClick={() => switchLocale('en')}>English</button>
            <button onClick={() => switchLocale('fr')}>French</button>
        </>
    )
}
