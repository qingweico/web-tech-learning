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
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" id="billRecordModal">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="账单类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                placeholder="请选择账单类型"
                v-decorator="['billType', { rules: [{ required: true, message: '请选择账单类型!' }] }]"
                @change="handleBillTypeChange"
              >
                <a-select-option :value="1">收入</a-select-option>
                <a-select-option :value="2">支出</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number
                placeholder="请输入金额"
                v-decorator="['amount', { rules: [{ required: true, message: '请输入金额!' }] }]"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                placeholder="请选择分类"
                v-decorator="['categoryId', { rules: [{ required: true, message: '请选择分类!' }] }]"
                showSearch
                :filterOption="filterOption"
              >
                <a-select-option v-for="item in categoryList" :key="item.id" :value="item.id">
                  {{ item.categoryName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="支付方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-form-item-rest>
                <JDictSelectTag
                  dictCode="PAYMENT"
                  placeholder="请选择支付方式"
                  type="select"
                  v-model="form.payment"
                  @input="paymentChange"
                  v-decorator="['payment',{ rules: [{ required: true, message: '请选择支付方式' }]}]"
                />
              </a-form-item-rest>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="账单时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker
                v-decorator="['billTime']"
                showTime
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择账单时间"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['remark']" placeholder="请输入备注" :rows="3" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { addRecord, editRecord, getCategoryListByType } from '@/api/bill'
import moment from 'moment'
import JMultiSelectTag from '@comp/dict/JMultiSelectTag.vue'
import { ajaxGetDictItems } from '@api/api'

export default {
  name: 'BillRecordModal',
  components: { JMultiSelectTag },
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
      confirmLoading: false,
      form: this.$form.createForm(this),
      categoryList: [],
      billType: null,
      paymentList: []
    }
  },
  created() {
    this.loadCategoryList()
  },
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.billType = record.billType

      this.$nextTick(() => {
        this.form.setFieldsValue({
          billType: record.billType,
          amount: record.amount,
          categoryId: record.categoryId,
          payment: record.payment,
          billTime: record.billTime ? moment(record.billTime) : null,
          remark: record.remark,
        })
        if (record.billType) {
          this.loadCategoryList(record.billType)
        }
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
          formData.billTime = formData.billTime ? formData.billTime.format('YYYY-MM-DD HH:mm:ss') : null

          let obj
          if (!this.model.id) {
            obj = addRecord(formData)
          } else {
            obj = editRecord(formData)
          }
          obj
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
              that.close()
            })
        }
      })
    },
    handleCancel() {
      this.close()
    },
    handleBillTypeChange(value) {
      this.billType = value
      this.loadCategoryList(value)
      this.form.setFieldsValue({ categoryId: undefined })
    },
    loadCategoryList(type) {
      const categoryType = type || this.billType
      if (!categoryType) {
        this.categoryList = []
        return
      }
      getCategoryListByType({ categoryType }).then((res) => {
        if (res.success) {
          this.categoryList = res.result || []
        }
      })
    },
    paymentChange(value) {
      this.form.setFieldsValue({ payment: value })
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
    },
  },
}
</script>

<style scoped></style>

