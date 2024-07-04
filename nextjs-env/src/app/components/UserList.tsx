'use client';
import {useState} from "react";
import {Button} from "primereact/button";

interface User {
    id: string,
    name: string,
    username: string,
    email: string,
    phone: string,
    website: string,
}

const users: User[] = [];
export default function UserList() {
    const [user, setUser] = useState(users);
    const getUser = async () => {
        const res = await fetch('/api/base');
        const {data} = await res.json()
        setUser(data);
    }
    return (
        <>
            <Button onClick={getUser}>获取 User</Button>
            {user.map(e => (
                <p key={e.id}>姓名 : {e.name}, 用户名 : {e.username}, 邮箱 : {e.email}, 电话 : {e.phone}, 网站
                    : {e.website}</p>
            ))}

        </>
    )
}
