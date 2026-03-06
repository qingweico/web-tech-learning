<template>
  <a-modal
    :title="title"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" id="televisionRecordModal">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="剧名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input
                placeholder="请输入剧名"
                v-decorator="['name', { rules: [{ required: true, message: '请输入剧名!' }] }]"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="集数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number
                placeholder="请输入集数"
                v-decorator="['episode', { initialValue: 0 }]"
                :min="0"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="观看状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag
                dict-code="watch_status"
                placeholder="请选择观看状态"
                type="select"
                v-model="form.watchStatus"
                @input="watchStatusChange"
                v-decorator="['watchStatus', { rules: [{ required: true, message: '请选择观看状态!' }] }]"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="评分" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number
                placeholder="请输入评分(1-10分)"
                v-decorator="['rating']"
                :min="1"
                :max="10"
                :precision="1"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select placeholder="请选择分类" v-decorator="['categoryId']" showSearch :filterOption="filterOption">
                <a-select-option v-for="item in categoryList" :key="item.id" :value="item.id">
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="标签" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                mode="multiple"
                placeholder="请选择标签（可多选）"
                v-decorator="['tagIds']"
                showSearch
                :filterOption="filterOption"
              >
                <a-select-option v-for="item in tagList" :key="item.id" :value="item.id">
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="开始观看时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker
                v-decorator="['startTime']"
                showTime
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择开始观看时间"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="结束观看时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker
                v-decorator="['endTime']"
                showTime
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择结束观看时间"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
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
import { addRecord, editRecord, getAllCategories, getAllTags } from '@/api/television'
import moment from 'moment'
import JDictSelectTag from '@comp/dict/JDictSelectTag.vue'

export default {
  name: 'TelevisionRecordModal',
  components: { JDictSelectTag },
  data() {
    return {
      title: '操作',
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      form: this.$form.createForm(this),
      categoryList: [],
      tagList: [],
    }
  },
  created() {
    this.loadCategoryList()
    this.loadTagList()
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
          name: record.name || undefined,
          episode: record.episode !== undefined ? record.episode : 0,
          watchStatus: record.watchStatus !== undefined ? record.watchStatus : undefined,
          rating: record.rating !== undefined ? record.rating : undefined,
          categoryId: record.categoryId || undefined,
          tagIds: record.tagIds || [],
          startTime: record.startTime ? moment(record.startTime) : null,
          endTime: record.endTime ? moment(record.endTime) : null,
          remark: record.remark || undefined,
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
          formData.startTime = formData.startTime ? formData.startTime.format('YYYY-MM-DD HH:mm:ss') : null
          formData.endTime = formData.endTime ? formData.endTime.format('YYYY-MM-DD HH:mm:ss') : null

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
    loadCategoryList() {
      getAllCategories().then((res) => {
        if (res.success && res.result) {
          this.categoryList = res.result
        }
      })
    },
    loadTagList() {
      getAllTags().then((res) => {
        if (res.success && res.result) {
          this.tagList = res.result
        }
      })
    },
    watchStatusChange(value) {
      this.form.setFieldsValue({ watchStatus: value })
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
    },
  },
}
</script>

<style scoped></style>
