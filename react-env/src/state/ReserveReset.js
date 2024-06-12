// state 的保留和重置
// 向组件传递一个唯一 key(会重新渲染组件当key发生变化时)
import {useState} from 'react';
import Chat from './Chat.js';
import ContactList from './ContactList.js';

export default function Messenger() {
    const [to, setTo] = useState(contacts[0]);
    return (<div>
        <ContactList contacts={contacts}
                     onSelect={(contact) => setTo(contact)}
                     selectedContact={to}></ContactList>
        <Chat key={to.email} contact={to}></Chat>
    </div>)

}
const contacts = [
    {name: 'Taylor', email: 'taylor@mail.com'},
    {name: 'Alice', email: 'alice@mail.com'},
    {name: 'Bob', email: 'bob@mail.com'}];
