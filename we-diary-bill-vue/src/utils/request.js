import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
import {
  VueAxios
} from './axios'
import {
  Modal,
  notification
} from 'ant-design-vue'
import {
  ACCESS_TOKEN
} from "@/store/mutation-types"

// 创建 axios 实例
const service = axios.create({
  baseURL: process.env.NODE_ENV === 'production' ?  ('http://'+location.hostname+'/we-diary-bill') : '/we-diary-bill',
  // baseURL: process.env.NODE_ENV === 'production' ?  ('http://'+location.hostname+':8094') : '/we-diary-bill',
  // baseURL: 'https://www.jdd966.com/jdd-boot',
  // baseURL: '', // api base_url
  timeout: 30000 // 请求超时时间
})

const err = (error) => {

  console.log(error, 'error=================')
  if (error.response) {
    console.log(error.response, 'error.response***************************')
    let data = error.response.data
    const token = Vue.ls.get(ACCESS_TOKEN)
    switch (error.response.status) {
      case 403:
        notification.error({
          message: '系统提示',
          description: data.message || '拒绝访问',
          duration: 4
        })
        break
      case 500:
        if (token && data.code == 402) {//token失效过期
          Modal.error({
            title: '登录已过期',
            content: '很抱歉，登录已过期，请重新登录',
            okText: '重新登录',
            mask: false,
            onOk: () => {
              store.dispatch('Logout').then(() => {
                Vue.ls.remove(ACCESS_TOKEN)
                window.location.reload()
              })
            }
          })

        }

        // if(token && data.message=="你已在其他终端登录，请重新登录!"){
        //   Modal.error({
        //     title: '重复登录',
        //     content: '你已在其他终端登录，请重新登录',
        //     okText: '重新登录',
        //     mask: false,
        //     onOk: () => {
        //       store.dispatch('Logout').then(() => {
        //         Vue.ls.remove(ACCESS_TOKEN)
        //         window.location.reload()
        //       })
        //     }
        //   })

        // }

        if (token && data.code == 413) {//token无效

          notification.error({
            message: '系统提示',
            description: 'token非法无效!',
            duration: 4
          })
        }

        if (token && data.code == 416) {//用户不存在

          notification.error({
            message: '系统提示',
            description: '用户不存在!',
            duration: 4
          })
        }

        if (token && data.code == 415) {//用户账号锁定

          notification.error({
            message: '系统提示',
            description: '账号已被锁定,请联系管理员!',
            duration: 4
          })
        }
        break
      case 404:
        notification.error({
          message: '系统提示',
          description: '很抱歉，资源未找到!',
          duration: 4
        })
        break
      case 504:
        notification.error({
          message: '系统提示',
          description: '网络超时'
        })
        break
      case 401:
        notification.error({
          message: '系统提示',
          description: '未授权，请重新登录',
          duration: 4
        })
        if (token) {
          store.dispatch('Logout').then(() => {
            setTimeout(() => {
              window.location.reload()
            }, 1500)
          })
        }
        break
      default:
        notification.error({
          message: '系统提示',
          description: data.message,
          duration: 4
        })
        break
    }
  }
  return Promise.reject(error)
};

// request interceptor
service.interceptors.request.use(config => {
  const token = Vue.ls.get(ACCESS_TOKEN)
  if (token) {
    config.headers['X-Access-Token'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
  }
  if (config.method == 'get') {
    config.params = {
      _t: Date.parse(new Date()) / 1000,
      ...config.params
    }
  }
  return config
}, (error) => {
  return Promise.reject(error)
})

// response interceptor
service.interceptors.response.use((response) => {

  // 刷新Token
  var accessToken = response.headers['x-access-token']

  if (accessToken) {
    Vue.ls.remove(ACCESS_TOKEN)
    /*Vue.ls.set(ACCESS_TOKEN, accessToken, 7 * 24 * 60 * 60 * 1000)
    */
    Vue.ls.set(ACCESS_TOKEN, accessToken)
    store.commit('SET_TOKEN', accessToken)
  }
  const token = Vue.ls.get(ACCESS_TOKEN);
  var returnCode = response.data.code;
  var msg = response.data.message;

  if(token && returnCode){

    if(401 == returnCode){//未授权
      notification.error({
        message: '系统提示',
        description: msg || '未授权，请联系管理员！',
        duration: 4
      })
    }else if(402 == returnCode){//token失效过期
      Modal.error({
        title: 'token已过期',
        content: msg || 'token已过期，请重新登录！',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN)
            window.location.reload()
          })
        }
      })
    }else if(405 == returnCode){//重复登录
      Modal.error({
        title: '重新登录',
        content: msg || '你已在其他终端登录，请重新登录!',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN)
            window.location.reload()
          })
        }
      })
    }else if(413 == returnCode){//token无效
      Modal.error({
        title: 'token无效',
        content: msg || 'token无效，请重新登录',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN)
            window.location.reload()
          })
        }
      })
    }else if(414 == returnCode){//token为空
      Modal.error({
        title: 'token为空',
        content: msg || 'token为空，请重新登录',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN)
            window.location.reload()
          })
        }
      })
    }else if(415 == returnCode){//用户账号锁定
      Modal.error({
        title: '用户账号锁定',
        content: msg || '用户账号锁定，请重新登录',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN)
            window.location.reload()
          })
        }
      })
    }else if(416 == returnCode){//用户不存在
      Modal.error({
        title: '用户不存在',
        content: msg || '用户不存在，请重新登录',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN)
            window.location.reload()
          })
        }
      })
    }else if(417 == returnCode){//非法用户
      Modal.error({
        title: '非法用户',
        content: msg || '非法用户，请重新登录',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          store.dispatch('Logout').then(() => {
            Vue.ls.remove(ACCESS_TOKEN)
            window.location.reload()
          })
        }
      })
    }else if(500 == returnCode){//服务器内部错误
      notification.error({
        message: '系统提示',
        description: response.data.msg || response.data.message,
        duration: 4
      })
    }
  }

  return response.data
}, err)

const installer = {
  vm: {},
  install(Vue, router = {}) {
    Vue.use(VueAxios, router, service)
  }
}

export {
  installer as VueAxios,
  service as axios
}
