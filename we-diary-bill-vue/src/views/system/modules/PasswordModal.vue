<template>
  <a-modal
      title="重新设定密码"
      :width="800"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @ok="handleSubmit"
      @cancel="handleCancel"
      cancelText="关闭"
      style="top:20px;"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="用户账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入用户账号" v-decorator="[ 'username', {}]" :readOnly="true"/>
        </a-form-item>

        <a-form-item label="新密码" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-input type="password" placeholder="请输入新密码" v-decorator="[ 'password', validatorRules.password]" />
        </a-form-item>

        <a-form-item label="确认新密码" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-input type="password" @blur="handleConfirmBlur" placeholder="请确认新密码" v-decorator="[ 'confirmpassword', validatorRules.confirmpassword]"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import {changPassword} from '@/api/api'

export default {
  name: "PasswordModal",
  data () {
    return {
      visible: false,
      confirmLoading: false,
      confirmDirty: false,
      validatorRules:{
        password:{
          rules: [{
            required: true, message: '密码由大写字母，小写字母和数字构成且不能少于8位!',
            pattern:/^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9A-Z]+$)(?![0-9a-z]+$)[0-9A-Za-z]{8,}$/
          }, {
            validator: this.validateToNextPassword,
          }],
        },
        confirmpassword:{
          rules: [{
            required: true, message: '请输入确认密码!',
          }, {
            validator: this.compareToFirstPassword,
          }],
        },
      },

      model: {},

      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      form:this.$form.createForm(this)
    }
  },
  created () {
    console.log("created");
  },

  methods: {
    show (id,username) {
      console.log(id)
      console.log(username)
      this.form.resetFields();
      this.visible = true;
      this.model.id = id;
      this.model.username = username;
      this.$nextTick(() => {
        this.form.setFieldsValue({username:username});
      });
    },
    close () {
      this.$emit('close');
      this.visible = false;
      this.disableSubmit = false;
      this.selectedRole = [];
    },
    handleSubmit () {
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          this.confirmLoading = true;
          let formData = Object.assign(this.model, values);
          changPassword(formData).then((res)=>{
            if(res.success){
              this.$message.success(res.message);
              this.$emit('ok');
            }else{
              this.$message.warning(res.message);
            }
          }).finally(() => {
            this.confirmLoading = false;
            this.close();
          });
        }
      })
    },
    handleCancel () {
      this.close()
    },
    validateToNextPassword  (rule, value, callback) {
      const form = this.form;
      const confirmpassword=form.getFieldValue('confirmpassword');
      if (value && confirmpassword && value !== confirmpassword) {
        callback('两次输入的密码不一样！');
      }
      if (value && this.confirmDirty) {
        form.validateFields(['confirm'], { force: true })
      }
      callback();
    },
    compareToFirstPassword  (rule, value, callback) {
      const form = this.form;
      if (value && value !== form.getFieldValue('password')) {
        callback('两次输入的密码不一样！');
      } else {
        callback()
      }
    },
    handleConfirmBlur  (e) {
      const value = e.target.value
      this.confirmDirty = this.confirmDirty || !!value
    }
  }
}
</script>
