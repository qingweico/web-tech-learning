export function Profile() {
    return (
        <img
            src="https://i.imgur.com/QIrZWGIs.jpg"
            alt="Alan L. Hart"
        />
    );
}

export default function Gallery() {
    return (
        <section>
            <h1>了不起的科学家们</h1>
            <Profile />
            <Profile />
            <Profile />
        </section>
    );
}
