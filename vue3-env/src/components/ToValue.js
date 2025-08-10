import { toValue } from 'vue'

function useFeature(maybeRefOrGetter) {
    // 如果 maybeRefOrGetter 是一个 ref 或 getter,
    // 将返回它的规范化值.
    // 否则原样返回.
    return toValue(maybeRefOrGetter)
}
export default useFeature
