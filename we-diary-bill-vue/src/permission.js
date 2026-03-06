import Vue from 'vue'
import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {
  ACCESS_TOKEN
} from '@/store/mutation-types'
import {
  generateIndexRouter
} from "@/utils/util"

NProgress.configure({
  showSpinner: false
}) // NProgress Configuration

const whiteList = ['/user/login', '/user/register', '/user/register-result', '/user/alteration'] // no redirect whitelist

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  if (Vue.ls.get(ACCESS_TOKEN)) {
    console.log(to.path);
    if (to.path === '/user/login') {/* has token */
      NProgress.done()
    } else {
      if (store.getters.permissionList.length === 0) {
        store.dispatch('GetPermissionList').then(res => {
          const menuData = res.result.menu;
          // console.log(res.message)
          if (menuData === null || menuData === "" || menuData === undefined) {
            return;
          }
          let constRoutes = [];
          constRoutes = generateIndexRouter(menuData);
          // 添加主界面路由
          store.dispatch('UpdateAppRouter', {
            constRoutes
          }).then(() => {
            // 根据roles权限生成可访问的路由表
            // 动态添加可访问路由表
            router.addRoutes(store.getters.addRouters, to.path)
            const redirect = Vue.ls.get('Watchhouse') ? "/watchhouse" : decodeURIComponent(from.query.redirect || to.path)
            console.log(redirect);
            if (to.path === redirect) {
              // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
              next({
                ...to,
                replace: true
              })
            } else {
              // 跳转到目的路由
              next({
                path: redirect
              })
            }
          })
        })
          .catch(() => {
            /* notification.error({
               message: '系统提示',
               description: '请求用户信息失败，请重试！'
             })*/

            console.log();
            store.dispatch('Logout').then(() => {
              next({
                path: '/user/login',
                query: {
                  redirect: Vue.ls.get('Watchhouse') ? "/" : to.fullPath
                }
              })
            })
          })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next({
        path: '/user/login',
        query: {
          redirect: Vue.ls.get('Watchhouse') ? "/" : to.fullPath
        }
      })
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
