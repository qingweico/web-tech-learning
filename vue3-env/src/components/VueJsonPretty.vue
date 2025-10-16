<template>
  <div>
    <el-button @click="refresh" :icon="Refresh" size="small" plain>刷新</el-button>

    <div v-if="loading" style="margin-top: 10px;">加载中...</div>
    <vue-json-pretty v-else
                     :data="users"
                     :show-line="true"
                     :show-line-number="false"
                     :showIcon="true"
                     :highlightMouseoverNode="true"
                     :highlightSelectedNode="true"
                     style="margin-top: 10px;"/>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {showFullScreenLoading} from "@/utils";
import {Refresh} from "@element-plus/icons-vue";

const users = ref([]);
const loading = ref(false);

async function refresh() {
  const loadingService = showFullScreenLoading();
  loading.value = true;
  try {
    const res = await fetch("https://randomuser.me/api/?results=2");
    const data = await res.json();
    users.value = data.results;
  } catch (err) {
    console.error("获取数据失败", err);
    users.value = [];
  } finally {
    loading.value = false;
    loadingService.close();
  }
}


refresh();
</script>
<style scoped>

</style>
