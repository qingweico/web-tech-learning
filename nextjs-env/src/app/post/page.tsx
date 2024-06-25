'use client'
import SwrPage from "@/app/navlink/swr";
import {useRouter} from 'next/router';

export default function Page() {
    const router = useRouter();
    const {id} = router.query;
    console.log(id)
    return <SwrPage id='1'></SwrPage>
}
