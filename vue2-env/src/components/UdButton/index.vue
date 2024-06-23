<template>
  <div>
    <slot name="left" />
    <el-button  :loading="loading" :disabled="disabledEdit" size="mini" type="primary" icon="el-icon-edit"  @click="doEdit" />
    <slot name="center" />
    <el-popover  v-model="pop"  placement="top" width="180" trigger="manual" @show="onPopoverShow" @hide="onPopoverHide">
      <p>{{ msg }}</p>
      <div style="text-align: right; margin: 0">
        <el-button size="mini" type="text" @click="doCancel">取消</el-button>
        <el-button type="primary" size="mini" @click="doDelete">确定</el-button>
      </div>
      <el-button slot="reference" style="margin-left: 10px" :loading="loading" :disabled="disabledDle" type="danger" icon="el-icon-delete" size="mini"  @click="toDelete" />
    </el-popover>
    <slot name="right" />
  </div>
</template>
<script>

export default {
  name: 'UdButton',
  props: {
    disabledDle: {
      type: Boolean,
      default: false
    },
    disabledEdit: {
      type: Boolean,
      default: false
    },
    msg: {
      type: String,
      default: '确定删除本条数据吗?'
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      pop: false
    }
  },
  methods: {
    doCancel() {
      this.pop = false
    },
    toDelete() {
      this.pop = true
    },
    doDelete() {
      this.$emit('doDelete')
      this.pop = false
    },
    doEdit() {
      this.$emit('doEdit')
    },
    onPopoverShow() {
      setTimeout(() => {
        document.addEventListener('click', this.handleDocumentClick)
      }, 0)
    },
    onPopoverHide() {
      document.removeEventListener('click', this.handleDocumentClick)
    },
    handleDocumentClick() {
      this.pop = false
    }
  }
}
</script>
