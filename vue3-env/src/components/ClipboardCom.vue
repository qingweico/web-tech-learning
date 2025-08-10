<template>
  <el-button @click="copyText">复制文本</el-button>
</template>

<script setup>
import useClipboard from 'vue-clipboard3';
import {ElMessage} from "element-plus";

const { toClipboard } = useClipboard();

const copyText = async () => {
  try {
    await toClipboard('要复制的文本');
    ElMessage.success('已复制到剪贴板')
  } catch (e) {
  }
};
</script>
