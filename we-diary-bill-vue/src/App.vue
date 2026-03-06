<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <router-view />
    </div>
  </a-config-provider>
</template>
<script>
  import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
  import enquireScreen from '@/utils/device'
  import { getAction} from '@/api/manage'

  export default {
    data () {
      return {
        locale: zhCN,
      }
    },
    methods:{
      /*解决监控页面时间token过期问题*/
      setTimer(){
        console.log("开启定时任务");
        setInterval(()=>{
          getAction("/sys/user/heartBeat");
        },10*60*1000);
      }
    },
    created () {
      let that = this
      enquireScreen(deviceType => {
        // tablet
        if (deviceType === 0) {
          that.$store.commit('TOGGLE_DEVICE', 'mobile')
          that.$store.dispatch('setSidebar', false)
        }
        // mobile
        else if (deviceType === 1) {
          that.$store.commit('TOGGLE_DEVICE', 'mobile')
          that.$store.dispatch('setSidebar', false)
        }
        else {
          that.$store.commit('TOGGLE_DEVICE', 'desktop')
          that.$store.dispatch('setSidebar', true)
        }
      });
      this.setTimer();
    }
  }
window.onload = function () {
  window.addEventListener(
    'mousewheel',
    function (event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    },
    { passive: false }
  )

  //firefox
  window.addEventListener(
    'DOMMouseScroll',
    function (event) {
      if (event.ctrlKey === true || event.metaKey) {
        event.preventDefault()
      }
    },
    { passive: false }
  )
}

</script>
<style>
#app {
  height: 100%;
}
</style>
