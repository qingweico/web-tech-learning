<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <div class="container">
      <div class="header">
        <table class="item">
          <tr>
            <td class="td__1">
              <img src="~@/assets/monitor/monitor@2x.png" alt="" srcset="" />
            </td>
            <td class="td__2">
              <span>{{ handOverData.workName }}</span>
            </td>
            <td class="td__3">上班时间：{{ handOverData.upTime }}</td>
          </tr>
          <tr>
            <td class="td__1"><img src="~@/assets/monitor/user@2x.png" alt="" srcset="" /></td>
            <td class="td__2">
              <span>{{ nickname() }}</span>
            </td>
            <td class="td__3">交班时间：{{ localCurrentTime }}</td>
          </tr>
        </table>
      </div>
      <div class="center">
        <table>
          <tr>
            <th></th>
            <th>人工收费（元）</th>
            <th>电子支付（元）</th>
          </tr>
          <tr>
            <td>应收金额</td>
            <td>{{ handOverData.manual_receivable }}</td>
            <td>{{ handOverData.online_receivable }}</td>
          </tr>
          <tr>
            <td>实收金额</td>
            <td>{{ handOverData.manual_paid }}</td>
            <td>{{ handOverData.online_paid }}</td>
          </tr>
          <tr>
            <td>免费金额</td>
            <td>{{ handOverData.manual_free }}</td>
            <td>{{ handOverData.online_free }}</td>
          </tr>
          <tr>
            <td>抵扣金额</td>
            <td>0</td>
            <td>0</td>
          </tr>
        </table>

        <table style="margin-left: 10px">
          <tr>
            <th>车流量信息</th>
          </tr>
          <tr>
            <td>总放行车：{{ handOverData.out_num }}</td>
          </tr>
          <tr>
            <td>免费放行：{{ handOverData.free_num }}</td>
          </tr>
          <tr>
            <td>异常放行：{{ handOverData.abnormal_num }}</td>
          </tr>
          <tr>
            <td>正常放行：{{ handOverData.normal_fee + handOverData.normal_no_fee }}</td>
          </tr>
        </table>
      </div>
      <div class="below">
        <a-form :form="form" layout="horizontal" :label-col="{ span: 4 }" :wrapper-col="{ span: 10 }" autocomplete="off">
          <a-form-item label="接班操作员">
            <a-input
              type="text"
              placeholder="输入接班操作员登录账号"
              v-decorator="['username', { rules: [{ required: true, message: '请输入账号' }] }]"
            />
          </a-form-item>
          <a-form-item label="密码">
            <a-input
              type="password"
              placeholder="输入登录密码"
              v-decorator="['password', { rules: [{ required: true, message: '请输入密码' }] }]"
            />
          </a-form-item>
          <a-form-item label="接班备注">
            <a-input type="text" placeholder="输入接班备注信息" v-decorator="['remark', {}]" />
          </a-form-item>
        </a-form>
      </div>
    </div>
  </a-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import { mapGetters } from 'vuex'
import { postAction } from '../../../api/manage'
import { encryption, getEncryptedString } from '@/utils/encryption/aesEncrypt'
import moment from 'moment'
export default {
  name: 'MonitorShiftModal',
  data() {
    return {
      title: '操作',
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      encryptedString: {
        key: '',
        iv: '',
      },
      result: {},
      confirmLoading: false,
      form: this.$form.createForm(this),
      url: {
        getHandOverLogList: '/operationLog/parkHandOverLog/getHandOverData',
        handOver: '/operationLog/parkHandOverLog/handOver',
        getUserId: '/sys/getUserId',
      },
      handOverData: {},
      localCurrentTime: '',
      timer: null,
      userId: '',
    }
  },
  created() {
    this.getEncrypte()
  },
  methods: {
    ...mapGetters(['nickname']),
    getUserId() {
      getAction(this.url.getUserId, {}).then((res) => {
        console.log(res.result + '----------点换班')
        if (res.code === 200) {
          this.userId = res.result
        }
      })
    },
    //点击换班访问数据
    getHandOverLog() {
      getAction(this.url.getUserId, {}).then((res) => {
        var parms = {}
        parms.userId = res.result
        getAction(this.url.getHandOverLogList, parms).then((res) => {
          if (res.result) {
            this.handOverData = res.result
            //赋值
          } else {
            //防止token失效也能换班
            this.visible=false;
            // this.$message.error(res.message)
          }
        })
      })
    },
    edit(record) {
      let _self = this
      _self.result.id = record
      _self.visible = true
      this.form.resetFields()
      this.getHandOverLog()
      this.launchTimer()
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    launchTimer() {
      this.timer = setInterval(() => {
        this.localCurrentTime = moment().format('YYYY-MM-DD HH:mm:ss')
      }, 1000)
    },
    stopTimer() {
      clearInterval(this.timer)
    },
    //强制获取密码加密规则
    getEncrypte() {
      getEncryptedString().then((data) => {
        this.encryptedString = data
      })
    },
    handleOk() {
      this.form.validateFields((err, values) => {
        if (!err) {
          let encryptStr = encryption(values.password, this.encryptedString.key, this.encryptedString.iv)
          values.password = encodeURI(encryptStr)
          postAction(this.url.handOver, values).then((res) => {
            if (res.success) {
              this.visible = false
              this.$emit('ok', res.result)
            }
          })
        }
      })
    },
    handleCancel() {
      this.close()
    },
  },
  watch: {
    visible(newVisible) {
      if (newVisible) {
        this.launchTimer()
      } else {
        this.stopTimer()
      }
    },
  },
}
</script>

<style lang="less">
.container {
  .header {
    width: 70%;
    margin: 0 auto;
    .item {
      font-size: 18px;
      font-weight: 400;
      width: 100%;

      img {
        width: 30px;
        height: 30px;
      }
      tr {
        margin-bottom: 20px;
        td {
          height: 54px;
        }
        .td__1 {
          width: 12%;
          text-align: center;
          line-height: 30px;
        }
        .td__2 {
          width: 36%;
          span {
            display: block;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
            width: 140px;
            font-size: 20px;
            color: #333333;
          }
        }
        .td__3 {
          white-space: nowrap;
          width: 52%;

          color: #666666;
        }
      }
    }
  }
  .center {
    margin-top: 25px;
    display: flex;
    justify-content: center;
    table {
      border: 1px solid #979797;
      tr th {
        background: #f5f5f5;
      }
      tr th,
      tr td {
        width: 180px;
        height: 54px;
        text-align: center;
      }
    }
  }
  position: relative;
  .below {
    margin-top: 30px;
    position: relative;
  }
}
</style>
