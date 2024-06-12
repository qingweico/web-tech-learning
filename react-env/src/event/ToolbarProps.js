// 将事件处理函数作为 props 传递
function Button({ onClick, children }) {
    return (
        <button onClick={onClick}>
            {children}
        </button>
    );
}

function PlayButton({ movieName }) {
    return (
        <Button onClick={() => {
            alert(`正在播放 ${movieName}`);
        }}>
            播放电影
        </Button>
    );
}
function UploadButton() {
    return (
        <Button onClick={() => alert('正在上传')}>
            上传图片
        </Button>
    );
}
export default function ToolbarProps() {
    return (
        <div>
            <PlayButton movieName="星球大战" />
            <UploadButton />
        </div>
    );
}
