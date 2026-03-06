<template>
  <div class="loginMain">
    <img class="logo" alt="logo_float" src="@/assets/logo.png" />
    <div class="form_wrap">
      <div class="right formWrap">
        <a-form :form="form" ref="formLogin" id="formLogin" class="formLogin">
          <div class="form_header">欢迎登录<span>蓝色时光</span></div>
          <div class="username_password_login">用户名密码登录</div>
          <a-form-item>
            <a-input
              v-decorator="['username', validatorRules.username]"
              type="text"
              class="username"
              placeholder="用户名"
            >
              <a-icon class="user-icon" slot="prefix" type="user" :style="{ color: 'rgba(66, 122, 238, 1)' }" />
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-input
              v-decorator="['password', validatorRules.password]"
              type="password"
              class="password"
              autocomplete="false"
              placeholder="密码"
            >
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(66, 122, 238, 1)' }" />
            </a-input>
          </a-form-item>

          <a-form-item class="checkboxItem">
            <a-checkbox v-model="formLogin.rememberMe">自动登录</a-checkbox>
          </a-form-item>

          <a-form-item>
            <a-button
              type="primary"
              htmlType="submit"
              class="login_button"
              :loading="loginBtn"
              @click.stop.prevent="handleSubmit"
              :disabled="loginBtn"
            >登录
            </a-button>
          </a-form-item>
          <div class="foot"></div>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script>
import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import { mapActions } from 'vuex'
import { timeFix, isEmpty } from '@/utils/util'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import JGraphicCode from '@/components/jeecg/JGraphicCode'
import { encryption, getEncryptedString } from '@/utils/encryption/aesEncrypt'

export default {
  components: {
    TwoStepCaptcha,
    JGraphicCode,
  },
  data() {
    return {
      src: 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: this.$form.createForm(this),
      encryptedString: {
        key: '',
        iv: '',
      },
      state: {
        time: 60,
        smsSendBtn: false,
      },
      formLogin: {
        username: '',
        password: '',
        captcha: '',
        mobile: '',
        rememberMe: true,
      },
      validatorRules: {
        username: {
          rules: [
            {
              required: true,
              message: '请输入用户名!',
            },
            {
              validator: this.handleUsernameOrEmail,
            },
          ],
        },
        password: {
          rules: [
            {
              required: true,
              message: '请输入密码!',
              validator: 'click',
            },
          ],
        },
        mobile: {
          rules: [
            {
              validator: this.validateMobile,
            },
          ],
        },
        captcha: {
          rule: [
            {
              required: true,
              message: '请输入验证码!',
            },
          ],
        },
      },
      verifiedCode: '',
      departList: [],
      departVisible: false,
      departSelected: '',
      currentUsername: '',
      validate_status: '',
      captchaSrc: '',
      verifiedCodess: '',
      province: undefined,
    }
  },
  created() {
    Vue.ls.remove(ACCESS_TOKEN)
    this.getRouterData()
    this.getEncrypte()
  },
  methods: {
    handleSubmit() {
      let that = this
      let loginParams = {
        remember_me: that.formLogin.rememberMe,
      }
      that.loginBtn = true
      that.form.validateFields(
        ['username', 'password', 'province'],
        {
          force: true,
        },
        (err, values) => {
          if (!err) {
            let { province } = values
            province && (this.selectedWorkStation = province)
            loginParams.username = values.username
            let encryptStr = encryption(values.password, that.encryptedString.key, that.encryptedString.iv)
            loginParams.password = encodeURI(encryptStr)
            that
              .Login(loginParams)
              .then(() => {
                if (isEmpty(province)) {
                  this.loginSuccess()
                  return
                }
              })
              .catch((err) => {
                that.requestFailed(err)
              })
          } else {
            that.loginBtn = false
          }
        }
      )
    },
    // 登录成功
    loginSuccess() {
      let tonane = 'dashboard'
      this.loginBtn = false
      if (this.province) {
        console.log('跳转岗亭监控')
        tonane = 'Watchhouse'
      }
      this.$router.push({
        name: tonane,
      })
      this.$notification.success({
        message: '欢迎',
        description: `${timeFix()}，欢迎回来`,
      })
    },
    ...mapActions(['Login', 'Logout', 'PhoneLogin']),
    // handler
    handleUsernameOrEmail(rule, value, callback) {
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        this.loginType = 0
      } else {
        this.loginType = 1
      }
      callback()
    },
    handleTabClick(key) {
      this.customActiveKey = key
      // this.form.resetFields()
    },

    stepCaptchaSuccess() {
      this.loginSuccess()
    },
    stepCaptchaCancel() {
      this.Logout().then(() => {
        this.loginBtn = false
        this.stepCaptchaVisible = false
      })
    },
    cmsFailed(err) {
      this.$notification['error']({
        message: '登录失败',
        description: err,
        duration: 4,
      })
    },
    requestFailed(err) {
      this.$notification['error']({
        message: '登录失败',
        description: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
        duration: 4,
      })
      this.loginBtn = false
    },
    validateMobile(rule, value, callback) {
      if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)) {
        callback()
      } else {
        callback('您的手机号码格式不正确!')
      }
    },
    departConfirm(res) {
      if (res.success) {
        let multi_depart = res.result.multi_depart
        //0:无部门 1:一个部门 2:多个部门
        if (multi_depart === 0) {
          this.loginSuccess()
        } else if (multi_depart === 2) {
          this.departVisible = true
          this.currentUsername = this.form.getFieldValue('username')
          this.departList = res.result.departs
        } else {
          this.loginSuccess()
        }
      } else {
        this.requestFailed(res)
        this.Logout()
      }
    },
    departChange(value) {
      this.validate_status = 'success'
      this.departSelected = value
    },
    getRouterData() {
      this.$nextTick(() => {
        this.form.setFieldsValue({
          username: this.$route.params.username,
        })
      })
    },
    getEncrypte() {
      getEncryptedString().then((data) => {
        this.encryptedString = data
      })
    },
  },
}
</script>

<style scoped>
html,
body {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  margin: 0;
  padding: 0;
}

html {
  font-size: 16px;
  min-width: 320px;
}

#app {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.loginMain {
  display: flex;
  font-family: MicrosoftYaHei, -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  width: 100vw;
  height: 100vh;
  background: url('~@/assets/loginbg@3x.png') no-repeat center center;
  background-size: cover;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.loginMain .logo {
  left: 3rem;
  top: 2.5rem;
  width: 8rem;
  height: 2.5rem;
  position: absolute;
}

.loginMain .form_wrap {
  width: 100%;
  max-width: 400px;
  min-width: 280px;
  height: auto;
  min-height: 420px;
  padding: 30px 20px;
  border-radius: 16px;
  background-color: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(10px);
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.9);
}

.loginMain .form_wrap:hover {
  box-shadow: 0 16px 56px rgba(0, 0, 0, 0.2);
  transform: translateY(-2px);
}

.loginMain .formWrap {
  position: relative;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.loginMain .formLogin {
  width: 100%;
  max-width: 320px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.formLogin .ant-form-item {
  margin-bottom: 20px;
  width: 100%;
}

.formLogin .checkboxItem {
  margin-bottom: 24px;
}

.formLogin .ant-form-item-control {
  line-height: 1.5;
}

.formLogin .checkboxItem .ant-form-item-control {
  line-height: normal;
}

.loginMain .form_header {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f1f1f;
  line-height: 1.4;
  margin-bottom: 0.5rem;
  text-align: center;
  word-break: break-word;
}

.loginMain .form_header span {
  color: #427aee;
  font-weight: 700;
  margin-left: 8px;
}

.loginMain .formLogin .ant-input {
  font-size: 14px;
  line-height: 1.5;
  padding: 8px 12px;
  height: 42px;
  background-color: #f9f9fa;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  transition: all 0.3s ease;
  padding-left: 36px !important;
}

.loginMain .formLogin .ant-input:hover,
.loginMain .formLogin .ant-input:focus {
  border-color: #427aee;
  box-shadow: 0 0 0 2px rgba(66, 122, 238, 0.1);
}

.loginMain .formLogin .ant-input-affix-wrapper .ant-input-prefix {
  margin-right: 8px;
}

.username_password_login {
  line-height: 1.3;
  font-size: 0.95rem;
  color: #427aee;
  margin: 1.2rem 0 1rem;
  font-weight: 500;
  text-align: center;
}

.login_button {
  width: 100%;
  height: 44px;
  background: #427aee;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(66, 122, 238, 0.25);
}



.login_button:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.loginMain .foot {
  font-size: 0.75rem;
  color: #666;
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  html {
    font-size: 14px;
  }

  .loginMain {
    padding: 15px;
  }

  .loginMain .logo {
    left: 15px;
    top: 15px;
    width: 100px;
    height: 35px;
  }

  .loginMain .form_wrap {
    max-width: 350px;
    padding: 25px 15px;
    border-radius: 12px;
  }

  .loginMain .form_header {
    font-size: 1.3rem;
  }

  .login_button {
    height: 42px;
    font-size: 15px;
  }

  .loginMain .formLogin .ant-input {
    height: 40px;
    font-size: 13px;
  }
}

@media screen and (max-width: 480px) {
  html {
    font-size: 12px;
  }

  .loginMain .logo {
    left: 10px;
    top: 10px;
    width: 80px;
    height: 28px;
  }

  .loginMain .form_wrap {
    max-width: 300px;
    padding: 20px 12px;
    border-radius: 10px;
  }

  .loginMain .form_header {
    font-size: 1.1rem;
  }

  .login_button {
    height: 40px;
    font-size: 14px;
    letter-spacing: 0.5px;
  }

  .loginMain .formLogin .ant-input {
    height: 38px;
    padding: 6px 10px;
  }

  .username_password_login {
    font-size: 0.85rem;
    margin: 1rem 0 0.8rem;
  }
}

@media screen and (max-width: 320px) {
  .loginMain .form_wrap {
    max-width: 280px;
    padding: 15px 10px;
  }

  .loginMain .form_header {
    font-size: 1rem;
  }

  .login_button {
    height: 38px;
    font-size: 13px;
  }
}

@media print {
  .loginMain {
    display: none;
  }
}

.loginMain .formLogin .ant-input-prefix {
  left: 12px;
  right: auto;
}
</style>
