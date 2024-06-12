// 事件会沿着父层级结构传播
export default function EventPropagation() {
    return (
        <div className="Toolbar" onClick={() => {
            alert('你点击了 toolbar');
        }}>
            <Button onClick={() => alert('正在播放')}>
                播放电影
            </Button>
            <Button onClick={() => alert('正在上传')}>
                上传图片
            </Button>
        </div>
    );
}
function Button({ onClick, children }) {
    return (
        // 阻止事件传播
        <button onClick={e => {
            e.stopPropagation();
            onClick();
        }}>
            {children}
        </button>
    );
}
