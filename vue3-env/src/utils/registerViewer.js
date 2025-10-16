import { getCurrentInstance } from 'vue'
import 'viewerjs/dist/viewer.css'
import Viewer from 'v-viewer'
// 可以在组件中单独注册也可以在main.js中全局注册
export default function registerViewer(
    app
) {
    let ac = null
    if(!app) {
        app = getCurrentInstance()?.appContext?.app
    }
    if(app && !app.config.globalProperties.$viewerApi) {
        app.use(Viewer)
        console.log('✅ 注册 Viewer 完成')
    } else {
        console.log('❌ 注册 Viewer 失败')
    }
}
