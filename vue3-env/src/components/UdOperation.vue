<template>
  <el-tooltip
      effect="dark"
      content="查看"
      placement="top-start"
  >
    <el-button type="success" @click="view">
      <slot>
        <el-icon>
          <View/>
        </el-icon>
      </slot>
    </el-button>
  </el-tooltip>
  <el-tooltip
      effect="dark"
      content="编辑"
      placement="top-start"
  >
    <el-button type="primary" @click="edit">
      <slot name="edit">
        <el-icon>
          <Edit/>
        </el-icon>
      </slot>
    </el-button>
  </el-tooltip>
  <el-tooltip
      effect="dark"
      content="删除"
      placement="top-start"
  >
    <el-button type="danger" @click="del">
      <slot name="del">
        <el-icon>
          <Delete/>
        </el-icon>
      </slot>
    </el-button>
  </el-tooltip>
  <el-tooltip
      effect="dark"
      content="设置"
      placement="top-start"
  >
    <el-button type="info" disabled @click="setting">
      <slot name="setting">
        <el-icon>
          <Setting/>
        </el-icon>
      </slot>
    </el-button>
  </el-tooltip>
  <br/>

  <div>
    <slot :text="'greetingMessage'" :count="1"></slot>
  </div>
</template>
<script setup>
// 带 name 的插槽被称为具名插槽 (named slots)
import {Delete, Edit, Setting, View} from "@element-plus/icons-vue";
import {ElNotification} from "element-plus";
import {useSlots} from "vue";

const $slots = useSlots();
const del = () => {
  ElNotification({
    title: '提示',
    message: '删除',
    icon: 'Cloudy'
  })
}

const edit = () => {
  ElNotification({
    title: '提示',
    message: '编辑',
    icon: 'Sunny'
  })
}

const view = () => {
  ElNotification({
    title: '提示',
    message: '查看',
    icon: 'Lightning'
  })
}
const setting = () => {
  ElNotification({
    title: '提示',
    message: '设置',
    icon: 'Sunrise'
  })
}
// console.log($slots)
</script>
