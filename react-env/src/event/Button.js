export default function Button() {
    function handleClick() {
        alert('You Clicked me!')
    }
    return (
        <button onClick={handleClick}>
            点我
        </button>
    );
}
