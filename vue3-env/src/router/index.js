import {
    createRouter,
    createWebHistory
} from 'vue-router'
import User from '@/views/UserHome.vue'
import NotFound from "@/views/NotFound.vue";
import UserProfile from "@/views/UserProfile.vue";
import UserSettings from "@/views/UserSettings.vue";
import UserDefault from "@/views/UserDefault.vue";
import UserCenter from "@/views/UserCenter.vue";
import HomePage from "@/views/HomePage.vue";
import DateTime from "@/views/DateTime.vue";
import SearchPage from "@/views/SearchPage.vue";
import {ElMessage} from "element-plus";
import NProgress from 'nprogress';
import LoginPage from "@/views/LoginPage.vue";
import {useAuthStore} from "@/store/authStore";

function removeQueryParams(to) {
    if (Object.keys(to.query).length)
        return {path: to.path, query: {}, hash: to.hash}
}

function removeHash(to) {
    if (to.hash) return {path: to.path, query: to.query, hash: ''}
}

const routes = [
    {
        path: '/',
        component: HomePage,
        meta: {requiresAuth: true},
        children: [
            // 用户ID 仅匹配数字
            {
                path: '/user/:id(\\d+)',
                // 匹配命名视图
                components: {
                    default: User,
                    time: DateTime
                },
                // 别名: 将UI 结构映射到一个任意的 URL, 而不受配置的嵌套结构的限制
                // 如果有参数记得带上参数
                alias: ['/u/:id(\\d+)'],
                // 将id 参数作为 prop 传递给组件
                // 对于有命名视图的路由, 你必须为每个命名视图定义 props 配置
                props: {default: true, time: true},
                // 路由独享的守卫
                // beforeEnter 守卫 只在进入路由时触发, 不会在 params、query 或 hash 改变时触发
                // 放在父级路由上, 路由在具有相同父级的子路由之间移动时, 它不会被触发
                beforeEnter: (to, from) => {
                    ElMessage({
                        message: 'beforeEnter',
                        type: 'success',
                    })
                    return true
                },
                // 嵌套路由
                children: [
                    {
                        path: 'profile',
                        name: 'profile',
                        component: UserProfile,
                        // 两个子路由之间移动时就会被调用
                        beforeEnter: (to, from) => {
                            ElMessage({
                                message: 'profile beforeEnter',
                                type: 'success',
                            })
                            return true
                        },
                    },
                    {
                        path: 'settings',
                        // 所有路由的命名都必须是唯一的, 如果为多条路由
                        // 添加相同的命名, 路由器只会保留最后那一条
                        name: 'settings',
                        component: UserSettings,
                    },
                    // 提供一个空的嵌套路径 children path 没有匹配到则渲染
                    {path: '', component: UserDefault},
                    {path: 'center', name: 'center', component: UserCenter},
                    // 相对重定向
                    {
                        // 将总是把/user/123/personal重定向到/user/123/profile.
                        path: 'personal',
                        redirect: to => {
                            // 该函数接收目标路由作为参数
                            // 相对位置不以`/`开头
                            // 或 { path: 'profile'}
                            // TODO
                            return 'profile'
                        },
                    }
                ],
            },
            // 将匹配所有内容并将其放在 `route.params.pathMatch` 下
            {path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound},
            // /:chapters -> 匹配 /one, /one/two, /one/two/three, 等
            {path: '/:chapters+'},
            // /:chapters -> 匹配 /, /one, /one/two, /one/two/three, 等
            {path: '/:chapters*'},
            // 星号表示匹配0个或者多个参数
            // 加号表示匹配1个或者多个参数, 所以在使用编程式路由传递参数时应该传递一个数组
            // 问号跟在参数后面, 表示这个参数可选, 但是该参数不能重复(正则中问号表示0个或1个)
            // TODO 嵌套的命名路由
        ]
    },
    {path: '/home', redirect: '/'},
    {path: '/home', redirect: {name: 'HomePage'}},
    {
        // url params 转 query
        // 可以省略 component 属性
        // 同时配置了导航守卫也不会生效,没有应用在跳转路由上, 而仅仅应用在其目标上
        path: '/search/:searchText',
        redirect: to => {
            return {path: '/search', query: {q: to.params.searchText}}
        }
    },
    {
        path: '/search',
        component: SearchPage,
        name: 'SearchPage',
        // 将 props 传递给路由组件的函数模式
        // 将 route query 作为 props 传递给组件 (静态值与基于路由的值相结合)
        // 尽可能保持 props 函数为无状态的, 因为它只会在路由发生变化时起作用
        // 还可以通过 <RouterView> 插槽向组件传递参数(不推荐, 因为所有视图组件都会接收到该参数)
        props: route => ({query: 'Q_' + route.query.q}),
        // 可以将一个函数数组传递给 beforeEnter
        // beforeEnter: [removeQueryParams, removeHash],
    },
    {
        path: '/login',
        name: 'Login',
        component: LoginPage
    }
]
// 创建路由器实例
const router = createRouter({
    history: createWebHistory(),
    routes,
    // 严格匹配(区分大小写 去区分尾部的斜线等等)
    strict: true,
})
// 全局前置守卫
router.beforeEach((to, from) => {
    NProgress.start();
    const authStore = useAuthStore();
    if (to.meta.requiresAuth && !authStore.isLoggedIn) {
        // 此路由需要授权, 请检查是否已登录
        // 如果没有, 则重定向到登录页面
        return {
            path: '/login',
            query: {redirect: to.fullPath},
        }
    }
    return true
})

// 全局解析守卫
router.beforeResolve(async to => {
    if (to.meta.requiresCamera) {

    }
})
// 全局后置钩子
router.afterEach((to, from, failure) => {
    // 全局导航故障
    if (failure) {
        ElMessage({
            message: '路由失败',
            type: 'error',
        })
    }
    NProgress.done(); // 结束进度条
});
router.addRoute({path: '/about', component: () => import('@/views/AboutPage.vue')})

/**
 * 添加嵌套路由
 * 将路由的 `name`作为第一个参数传递给 {@link VueRouter#addRoute()}
 * 这样后面的 route 将会作为第一个 route 的 `children`
 */

/**
 * 删除路由
 * 1: 添加一个名称冲突的路由
 * 2: 调用 {@link VueRouter#addRoute()} 返回的回调
 * 3: 使用 {@link VueRouter#removeRoute()} 按名称删除路由
 * 当路由被删除时, 所有的别名和子路由也会被同时删除
 *
 */
/**
 * 查看现有路由
 *
 * 获取一个包含所有路由记录的数组
 * @see VueRouter#getRoutes()
 * 检查路由是否存在
 * @see VueRouter#hasRoute()
 */

// $router 路由器实例
// $route 当前路由
export default router
