export default function ContactList({
                                        selectedContact,
                                        contacts,
                                        onSelect
                                    }) {
    return (
        <>
            <section className="contact-list">
                <ul>
                    {contacts.map(e =>
                        <li key={e.email}>
                            <button onClick={() => {
                                onSelect(e)
                            }}>
                                {e.name}
                            </button>

                        </li>
                    )
                    }
                </ul>
            </section>
        </>
    )
}
