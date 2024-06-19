import {redirect} from 'next/navigation'

function getToken(id: string) {
    console.log(id)
    return window.localStorage.getItem("Authorization");
}

export default async function beforeRouter({params}: { params: { id: string } }) {
    const token = getToken(params.id)
    if (!token) {
        redirect('/login')
    }
}
