<template>
  <div class="page-header-index-wide">
    <a-row :gutter="16" style="margin-left:20px;">
      <a-col :md="24" :lg="8">
        <a-form layout="vertical" :form="form">
          <a-form-item label="头像">
            <a-upload
              listType="picture-card"
              class="avatar-uploader"
              :showUploadList="false"
              :action="uploadAction"
              :data="{'isup':1}"
              :headers="headers"
              :beforeUpload="beforeUpload"
              @change="handleChange"
            >
              <img
                v-if="picUrl"
                :src="getAvatarView()"
                alt="头像"
                style="height:104px;max-width:300px"
              />
              <div v-else>
                <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
                <div class="ant-upload-text">上传</div>
              </div>
            </a-upload>
          </a-form-item>

          <a-form-item label="生日">
            <a-date-picker
              style="width: 100%"
              placeholder="请选择生日"
              v-decorator="['birthday', {initialValue:!model.birthday?null:moment(model.birthday,this.dateFormat)}]"
            />
          </a-form-item>

          <a-form-item label="性别">
            <a-select v-decorator="[ 'sex', {}]" placeholder="请选择性别">
              <a-select-option :value="1">男</a-select-option>
              <a-select-option :value="2">女</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="邮箱">
            <a-input placeholder="请输入邮箱" v-decorator="[ 'email', validatorRules.email]" />
          </a-form-item>

          <a-form-item label="手机号码">
            <a-input placeholder="请输入手机号码" v-decorator="[ 'phone', validatorRules.phone]" />
          </a-form-item>

          <a-form-item>
            <a-button type="primary" @click="handleSubmit">保存</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>

    <avatar-modal ref="modal"></avatar-modal>
  </div>
</template>

<script>
import moment from 'moment'
import Vue from 'vue'
import pick from 'lodash.pick'
import AvatarModal from './AvatarModal'
import { editUser } from '@/api/api'
import { getAction } from '@/api/manage'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  components: {
    AvatarModal
  },
  data() {
    return {
      form: this.$form.createForm(this),
      dateFormat: 'YYYY-MM-DD',
      // cropper
      preview: {},
      model: {},
      picUrl: '',
      uploadLoading: false,
      validatorRules: {
        password: {
          rules: [
            {
              required: true,
              // pattern:/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/,
              message: '密码由8位数字、大小写字母和特殊符号组成!'
            },
            {
              validator: this.validateToNextPassword
            }
          ]
        },
        confirmpassword: {
          rules: [
            {
              required: true,
              message: '请重新输入登陆密码!'
            },
            {
              validator: this.compareToFirstPassword
            }
          ]
        },
        phone: { rules: [{ validator: this.validatePhone }] },
        email: {
          rules: [
            {
              validator: this.validateEmail
            }
          ]
        }
      },
      option: {
        img: '/avatar2.jpg',
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      },
      url: {
        fileUpload: window._CONFIG['domianURL'] + '/sys/common/upload',
        imgerver: window._CONFIG['domianURL'] + '/sys/common/view',
        queryUser: '/sys/user/queryUser'
      },
      headers: {}
    }
  },

  created() {
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.headers = { 'X-Access-Token': token }

    getAction(this.url.queryUser).then(res => {
      if (res.success) {
        let record = res.result
        // this.$form = document.getElementById('personcenter');

        //  console.log(JSON.stringify(record));
        this.picUrl = record.avatar
        this.model = Object.assign({}, record)
        this.form.setFieldsValue(pick(record, 'birthday', 'sex', 'email', 'mobile', 'phone'))
      }
      if (res.code === 510) {
        this.$message.warning(res.message)
      }
    })
  },

  computed: {
    uploadAction: function() {
      return this.url.fileUpload
    }
  },
  methods: {
    moment,
    handleSubmit() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let avatar = that.model.avatar
          // if(!values.birthday){
          //   values.birthday = '';
          // }else{
          //   values.birthday = values.birthday.format(this.dateFormat);
          // }
          let formData = Object.assign(this.model, values)

          formData.avatar = avatar

          editUser(formData)
            .then(res => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },

    handleChange(info) {
      this.picUrl = ''
      if (info.file.status === 'uploading') {
        this.uploadLoading = true
        return
      }
      if (info.file.status === 'done') {
        var response = info.file.response
        this.uploadLoading = false
        if (response.success) {
          this.model.avatar = response.result
          
          this.picUrl = 'Has no pic url yet'
        } else {
          this.$message.warning(response.message)
        }
      }
    },
    getAvatarView() {
      return this.model.avatar
    },
    beforeUpload: function(file) {
      var fileType = file.type
      if (fileType.indexOf('image') < 0) {
        this.$message.warning('请上传图片')
        return false
      }
      //TODO 验证文件大小
    },

    validatePhone(rule, value, callback) {
      if (!value) {
        callback()
      } else {
        if (new RegExp(/^1[3|4|5|7|8][0-9]\d{8}$/).test(value)) {
          var params = {
            tableName: 'sys_user',
            fieldName: 'phone',
            fieldVal: value,
            dataId: this.userId
          }
          duplicateCheck(params).then(res => {
            if (res.success) {
              callback()
            } else {
              callback('手机号已存在!')
            }
          })
        } else {
          callback('请输入正确格式的手机号码!')
        }
      }
    },
    validateEmail(rule, value, callback) {
      if (!value) {
        callback()
      } else {
        if (new RegExp(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/).test(value)) {
          var params = {
            tableName: 'sys_user',
            fieldName: 'email',
            fieldVal: value,
            dataId: this.userId
          }
          duplicateCheck(params).then(res => {
            if (res.success) {
              callback()
            } else {
              callback('邮箱已存在!')
            }
          })
        } else {
          callback('请输入正确格式的邮箱!')
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.avatar-upload-wrapper {
  height: 200px;
  width: 100%;
}

.page-header-index-wide {
  background: #fff;
  padding: 10px 10px;
}

.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }
  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img,
  .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 50%;
    overflow: hidden;
  }
}
</style>