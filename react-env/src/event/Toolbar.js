function AlertButton({message, children}) {
    return (
        <button onClick={ () => {
            alert(message)
        }}>
            {children}
        </button>
    )
}

 export default function Toolbar() {
    return <>
        <AlertButton message={'正在播放'} children={'播放电影'}></AlertButton>
        <AlertButton message={'正在上传'} children={'上传文件'}></AlertButton>
    </>
}
