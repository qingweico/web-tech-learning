<template>
  <div
    class="user-wrapper"
    :class="theme"
  >
    <a-dropdown>
      <span class="action action-full ant-dropdown-link user-dropdown-menu">
        <a-avatar
          class="avatar"
          size="small"
          :src="getAvatar()"
        />
        <span v-if="isDesktop()">欢迎您，{{ nickname() }}</span>
      </span>
      <a-menu
        slot="overlay"
        class="user-dropdown-menu-wrapper"
      >
        <a-menu-item
          key="3"
          @click="systemSetting"
        >
          <a-icon type="tool" />
          <span>系统设置</span>
        </a-menu-item>
        <a-menu-item
          key="2"
          @click="updatePassword"
        >
          <a-icon type="setting" />
          <span>密码修改</span>
        </a-menu-item>
      </a-menu>
    </a-dropdown>
    <span class="action">
      <a
        class="logout_title"
        href="javascript:;"
        @click="handleLogout"
      >
        <a-icon type="logout" />
        <span v-if="isDesktop()">&nbsp;退出登录</span>
      </a>
    </span>
    <user-password
      @loginOut="loginOut"
      ref="userPassword"
    ></user-password>

    <setting-drawer
      ref="settingDrawer"
      :closable="true"
      title="系统设置"
    ></setting-drawer>
    <!-- <todo-notice-modal :confirmLoading="confirmLoading" ref="modalForm"></todo-notice-modal>-->
    <a-modal
      :title="'选择管理工作站'"
      :width="800"
      :visible="visible"
      @ok="bindWorkStation"
      :ok-button-props="{props:{disabled:!selectedWorkStation}}"
      @cancel="handleCancel"
      cancelText="关闭"
    >
      <a-form>
        <a-col>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="工作站"
          >
            <a-select
              placeholder="请选择"
              @change="workStationChange"
            >
              <a-select-option
                v-for="workStation in workStationList"
                :key="workStation.id"
              >{{ workStation.workName }}</a-select-option>
            </a-select>
            说明：在下拉框中选择一个工作站，点击确定后即可进入监控。
          </a-form-item>
        </a-col>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import UserPassword from './UserPassword'
import SettingDrawer from '@/components/setting/SettingDrawer'

import { mapActions, mapGetters } from 'vuex'
import { mixin, mixinDevice } from '@/utils/mixin.js'
import { startsWith } from 'lodash'
import { httpAction, postAction, getAction } from '@/api/manage'
import Vue from 'vue'
import { USER_INFO } from '@/store/mutation-types'

export default {
  name: 'UserMenu',
  mixins: [mixin, mixinDevice],
  data() {
    return {
      visible: false,
      confirmLoading: true,
      parkInfo: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      workStationList: [],
      selectedWorkStation: '',
      url: {
        bindWorkStation: '/parking/parkWorkStation/bindWorkStation',
        synchronizeConfiguration: '/parking/parkInfo/synchronizeConfiguration',
        parkSyncOption: '/parking/parkInfo/parkSyncOption',
      },
    }
  },
  components: {
    UserPassword,
    SettingDrawer,
  },
  props: {
    theme: {
      type: String,
      required: false,
      default: 'dark',
    },
  },
  computed: {
    isNotLebaOrSQAccount: function () {
      let username = this.userInfo().username
      return username != 'leba' && username != 'guosiqiang'
    },
  },
  created() {},
  mounted() {
    let _self = this
    _self.init()
  },
  methods: {
    ...mapActions(['Logout']),
    ...mapGetters(['nickname', 'avatar', 'userInfo']),
    init() {
      let _self = this
    },
    handleOk() {
      let _self = this
      _self.visible = !_self.visible
      _self.requestFullScreen()
      _self.$router.push('/monitor')
    },
    handleCancel() {
      let _self = this
      _self.visible = !_self.visible
    },
    toZombie() {

    },
    synchronizeConfiguration() {
      postAction(this.url.synchronizeConfiguration, {}).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
        } else {
          this.$message.error(res.message)
        }
      })
    },
    checkStationAuth() {
      getAction('/parking/parkWorkStation/checkStationAuth', {}).then((ret) => {
        console.log(ret)
        if (ret.result) {
          this.bindWorkStationNoSelect()
        } else {
          this.showMonitorPage()
        }
      })
    },
    bindWorkStationNoSelect() {
      postAction('/parking/parkWorkStation/bindWorkStationNoSelect', {}).then((res) => {
        if (res.code === 200) {
          this.requestFullScreen()
          this.$router.push('/monitor')
        }
      })
    },
    showMonitorPage() {
      this.getWorkStationList()
      let _self = this
      _self.visible = !_self.visible
    },
    requestFullScreen() {
      var docElm = document.documentElement
      if (docElm.requestFullscreen) {
        docElm.requestFullscreen()
      } else if (docElm.msRequestFullscreen) {
        docElm.msRequestFullscreen()
      } else if (docElm.mozRequestFullScreen) {
        docElm.mozRequestFullScreen()
      } else if (docElm.webkitRequestFullScreen) {
        docElm.webkitRequestFullScreen()
      }
    },
    switchRule(parkInfo) {
      const that = this
      var chargeMode = parkInfo.chargeMode
      if (chargeMode === '1') {
        this.$confirm({
          title: '确认切换计费模式吗',
          content: '是否切换到场端计费模式?',
          onOk: function () {
            parkInfo.chargeMode = '0'
            postAction('/parking/parkInfo/editParkInfo', parkInfo).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
              } else {
                that.$message.warning(res.message)
              }
            })
          },
        })
      }
      if (chargeMode === '0') {
        this.$confirm({
          title: '确认切换计费模式吗',
          content: '是否切换到云端计费模式?',
          onOk: function () {
            parkInfo.chargeMode = '1'
            httpAction(this.url.parkSyncOption, parkInfo, 'post').then((res) => {
              if (res.success) {
                that.$message.success(res.message)
              } else {
                if (res.result) {
                  that.$message.warning(res.message + ':' + res.result)
                } else {
                  that.$message.warning(res.message)
                }
              }
            })
          },
        })
      }
    },
    getAvatar() {
      // console.log(this.avatar(), 'this.avatar()')
      return this.avatar()
        ? startsWith(this.avatar(), 'http')
          ? this.avatar()
          : window._CONFIG['domianURL'] + '/' + this.avatar()
        : ''
    },
    handleLogout() {
      const that = this

      this.$confirm({
        title: '提示',
        content: '真的要注销登录吗 ?',
        onOk() {
          that.loginOut()
        },
        onCancel() {},
      })
    },
    loginOut() {
      const that = this
      return that
        .Logout({})
        .then(() => {
          // window.location.href="/";
          window.location.reload()
          // console.log(window.location)
        })
        .catch((err) => {
          that.$message.error({
            title: '错误',
            description: err.message,
          })
        })
    },
    updatePassword() {
      let username = this.userInfo().username
      this.$refs.userPassword.show(username)
    },

    systemSetting() {
      this.$refs.settingDrawer.showDrawer()
    },
    getWorkStationList() {
      getAction('/parking/parkWorkStation/noUseWorkStation', {}).then((ret) => {
        console.log(ret)
        this.workStationList = ret.result
      })
    },
    workStationChange(value) {
      this.selectedWorkStation = value
    },
    saveMacToStorage() {
      //将本机mac保存到storage中
      return new Promise((resolve) => {
        let LODOP = window.getLodop()
        LODOP.GET_SYSTEM_INFO('NetworkAdapter.1.PhysicalAddress')
        if (LODOP.CVERSION) {
          window.CLODOP.On_Return = function (TaskID, Value) {
            Vue.ls.set('mac', Value)
            resolve()
          }
        }
      })
    },
    bindWorkStation() {
      let userInfo = Vue.ls.get(USER_INFO)
      let mac = Vue.ls.get('mac')
      if (mac === null) {
        this.saveMacToStorage().then(() => {
          this.bindWorkStation()
        })
        return
      }
      postAction(this.url.bindWorkStation, {
        id: this.selectedWorkStation,
        operationName: userInfo.username,
        realName: userInfo.realname,
        mac: mac,
      }).then((ret) => {
        if (ret.success) {
          this.handleOk()
        }
      })
    },
  },
}
</script>

<style scoped>
.logout_title {
  color: inherit;
  text-decoration: none;
}

.monitorButton:hover {
  background-color: #e7f3fe;
}

.monitorButton img {
  height: 64px;
}
</style>
