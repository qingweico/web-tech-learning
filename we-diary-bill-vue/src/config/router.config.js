import {
  UserLayout,
  TabLayout,
  RouteView,
  BlankLayout
} from '@/components/layouts'

/**
 * 走菜单，走权限控制
 * @type {[null,null]}
 */
export const asyncRouterMap = [

  {
    path: '/',
    name: 'dashboard',
    component: TabLayout,
    meta: {
      title: '首页'
    },
    redirect: '/dashboard/workplace',
    children: [

      // dashboard
      {
        path: '/dashboard',
        name: 'dashboard',
        redirect: '/dashboard/workplace',
        component: RouteView,
        meta: {
          title: '仪表盘',
          icon: 'dashboard',
          permission: ['dashboard']
        },
        children: [{
            path: '/dashboard/analysis',
            name: 'Analysis',
            component: () => import('@/views/dashboard/Analysis'),
            meta: {
              title: '分析页',
              permission: ['dashboard']
            }
          }
        ]
      },
      // Exception
      {
        path: '/exception',
        name: 'exception',
        component: RouteView,
        redirect: '/exception/403',
        meta: {
          title: '异常页',
          icon: 'warning',
          permission: ['exception']
        },
        children: [{
            path: '/exception/403',
            name: 'Exception403',
            component: () => import( /* webpackChunkName: "fail" */ '@/views/exception/403'),
            meta: {
              title: '403',
              permission: ['exception']
            }
          },
          {
            path: '/exception/404',
            name: 'Exception404',
            component: () => import( /* webpackChunkName: "fail" */ '@/views/exception/404'),
            meta: {
              title: '404',
              permission: ['exception']
            }
          },
          {
            path: '/exception/500',
            name: 'Exception500',
            component: () => import( /* webpackChunkName: "fail" */ '@/views/exception/500'),
            meta: {
              title: '500',
              permission: ['exception']
            }
          }
        ]
      },
      // account
      {
        path: '/account',
        component: RouteView,
        name: 'account',
        meta: {
          title: '个人页',
          icon: 'user',
          keepAlive: true,
          permission: ['user']
        },
        children: [{
            path: '/account/center',
            name: 'center',
            component: () => import('@/views/account/center/Index'),
            meta: {
              title: '个人中心',
              keepAlive: true,
              permission: ['user']
            }
          },
          {
            path: '/account/settings',
            name: 'settings',
            component: () => import('@/views/account/settings/Index'),
            meta: {
              title: '个人设置',
              hideHeader: true,
              keepAlive: true,
              permission: ['user']
            },
            redirect: '/account/settings/base',
            alwaysShow: true,
            children: [{
                path: '/account/settings/base',
                name: 'BaseSettings',
                component: () => import('@/views/account/settings/BaseSetting'),
                meta: {
                  title: '基本设置',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
              {
                path: '/account/settings/security',
                name: 'SecuritySettings',
                component: () => import('@/views/account/settings/Security'),
                meta: {
                  title: '安全设置',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
              {
                path: '/account/settings/custom',
                name: 'CustomSettings',
                component: () => import('@/views/account/settings/Custom'),
                meta: {
                  title: '个性化设置',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
              {
                path: '/account/settings/binding',
                name: 'BindingSettings',
                component: () => import('@/views/account/settings/Binding'),
                meta: {
                  title: '账户绑定',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
              {
                path: '/account/settings/notification',
                name: 'NotificationSettings',
                component: () => import('@/views/account/settings/Notification'),
                meta: {
                  title: '新消息通知',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
            ]
          },
        ]
      },
      // bill
      {
        path: '/bill',
        component: RouteView,
        name: 'bill',
        redirect: '/bill/record',
        meta: {
          title: '记账管理',
          icon: 'wallet',
          permission: ['bill']
        },
        children: [
          {
            path: '/bill/record',
            name: 'BillRecordList',
            component: () => import('@/views/bill/BillRecordList'),
            meta: {
              title: '账单记录',
              permission: ['bill']
            }
          },
          {
            path: '/bill/category',
            name: 'BillCategoryList',
            component: () => import('@/views/bill/BillCategoryList'),
            meta: {
              title: '分类管理',
              permission: ['bill']
            }
          }
        ]
      },
      // television
      {
        path: '/television',
        component: RouteView,
        name: 'television',
        redirect: '/television/record',
        meta: {
          title: '追剧管理',
          icon: 'video-camera',
          permission: ['television']
        },
        children: [
          {
            path: '/television/category',
            name: 'TelevisionCategoryList',
            component: () => import('@/views/television/TelevisionCategoryList'),
            meta: {
              title: '观影分类',
              permission: ['television']
            }
          },
          {
            path: '/television/tag',
            name: 'TelevisionTagList',
            component: () => import('@/views/television/TelevisionTagList'),
            meta: {
              title: '观影标签',
              permission: ['television']
            }
          },
          {
            path: '/television/record',
            name: 'TelevisionRecordList',
            component: () => import('@/views/television/TelevisionRecordList'),
            meta: {
              title: '追剧记录',
              permission: ['television']
            }
          },
          {
            path: '/television/statistics',
            name: 'TelevisionStatistics',
            component: () => import('@/views/television/TelevisionStatistics'),
            meta: {
              title: '统计图表',
              permission: ['television']
            }
          }
        ]
      }
    ]
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  },

]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [{
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [{
        path: 'login',
        name: 'login',
        component: () => import( /* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import( /* webpackChunkName: "user" */ '@/views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import( /* webpackChunkName: "user" */ '@/views/user/RegisterResult')
      },
      {
        path: 'alteration',
        name: 'alteration',
        component: () => import( /* webpackChunkName: "user" */ '@/views/user/Alteration')
      },
    ]
  },
  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [{
      path: 'home',
      name: 'TestHome',
      component: () => import('@/views/Home')
    }]
  },
  {
    path: '/404',
    component: () => import( /* webpackChunkName: "fail" */ '@/views/exception/404')
  },

]
