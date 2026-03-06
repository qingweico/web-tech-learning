<template>
  <a-modal
    :title="title"
    :width="600"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="分类名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            placeholder="请输入分类名称"
            v-decorator="['categoryName', {rules: [{ required: true, message: '请输入分类名称!' }]}]"/>
        </a-form-item>
        <a-form-item label="分类类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select
            placeholder="请选择分类类型"
            v-decorator="['categoryType', {rules: [{ required: true, message: '请选择分类类型!' }]}]">
            <a-select-option :value="1">收入</a-select-option>
            <a-select-option :value="2">支出</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number
            v-decorator="['sortOrder', {initialValue: 0}]"
            :min="0"
            style="width: 100%"/>
        </a-form-item>
        <a-form-item label="图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            placeholder="请输入图标（可选）"
            v-decorator="['icon']"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea
            v-decorator="['remark']"
            placeholder="请输入备注"
            :rows="3"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { addCategory, editCategory } from '@/api/bill'

export default {
  name: 'BillCategoryModal',
  data() {
    return {
      title: '操作',
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      form: this.$form.createForm(this)
    }
  },
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue({
          categoryName: record.categoryName,
          categoryType: record.categoryType,
          sortOrder: record.sortOrder,
          icon: record.icon,
          remark: record.remark
        })
      })
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      const that = this
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let formData = Object.assign(this.model, values)
          let obj
          if (!this.model.id) {
            obj = addCategory(formData)
          } else {
            obj = editCategory(formData)
          }
          obj.then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
            that.close()
          })
        }
      })
    },
    handleCancel() {
      this.close()
    }
  }
}
</script>

<style scoped>
</style>


