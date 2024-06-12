export default function Signup() {
    return (
        <form onSubmit={e => {
            // 阻止少数事件的默认浏览器行为
            // 表单提交后后重新加载整个页面
            e.preventDefault();
            alert('提交表单');
        }}>
            <input/>
            <button>发送</button>
        </form>
    );
}
