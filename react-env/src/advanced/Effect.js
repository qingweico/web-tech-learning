// useEffect 比对 Vue 中的 nextTick
// useEffect 可以在组件挂载后执行, 也可以在组件更新后执行(如果提供了依赖数组)



import {useState, useRef, useEffect} from "react";

export default function EffectSync() {
    const [isPlaying, setIsPlaying] = useState(false);
    return (<>
        <button onClick={() => {
            setIsPlaying(!isPlaying);

        }}>{isPlaying ? '暂停' : '播放'}</button>
        <VideoPlayer isPlaying={isPlaying}
                     src='https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4'/>
    </>)
}


function VideoPlayer({src, isPlaying}) {
    const ref = useRef(null);
    useEffect(() => {
        if (isPlaying) {
            ref.current.play();
        } else {
            ref.current.pause();
        }
    }, [isPlaying])

    return <video ref={ref} src={src} loop playsInline/>;
}
