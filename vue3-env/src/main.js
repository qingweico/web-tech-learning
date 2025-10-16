import {createApp} from 'vue'
import App from './App.vue'
import SecretPiniaPlugin from './store/plugin'
import 'element-plus/dist/index.css';
import i18n from './i18n';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
// Element Plus
import ElementPlus from 'element-plus';
import 'nprogress/nprogress.css';
import zhCn from 'element-plus/es/locale/lang/zh-cn';
import {createPinia} from 'pinia'
import i18nPlugin from './plugins/i18n'
import router from './router/index'
import VueJsonPretty from 'vue-json-pretty'
import 'vue-json-pretty/lib/styles.css'
const app = createApp(App)
const pinia = createPinia()
pinia.use(SecretPiniaPlugin)
// 返回包含特定属性的对象  为每个 store 都添加上特定属性
pinia.use(() => ({ hello: 'world' }))
// 可以直接在 store 上设置该属性
pinia.use(({ store }) => {
    store.version = 'Vue3'
})
// 上文示例
pinia.use(({ store }) => {
    store.hello = 'world'
    // 确保你的构建工具能处理这个问题,webpack 和 vite 在默认情况下应该能处理.
    if (process.env.NODE_ENV === 'development') {
        // 添加你在 store 中设置的键值
        store._customProperties.add('hello')
    }
})

// Register Element Plus Icons
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

app.use(ElementPlus, {
    locale: zhCn,
});
// vue3 推荐使用 Pinia, vuex 目前已经维护
app.use(pinia)

app.directive('focus', {
    mounted(el) {
        el.focus()
    }
})
app.directive('highlight', {
    mounted(el, binding, vnode) {
        el.style.backgroundColor = binding.value
    }

})
app.directive('color', (el, binding, vnode) => {
    // 这会在 `mounted` 和 `updated` 时都调用
    el.style.color = binding.value
})

app.use(i18nPlugin, {
    common: {
        delete: '删除',
        email: '邮件'
    }
})
/**
 *  将路由器实例注册为插件
 *    -- 全局注册 RouterView 和 RouterLink 组件
 *    -- 添加全局 $router 和 $route 属性
 *    -- 启用 useRouter() 和 useRoute() 组合式函数
 *    -- 触发路由器解析初始路由
 */
app.use(router)
app.use(i18n);
app.component('vue-json-pretty', VueJsonPretty)
app.mount('#app');
