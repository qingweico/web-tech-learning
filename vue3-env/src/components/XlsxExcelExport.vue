<template>
  <div class="excel-export">
    <h2>Excel 导出测试（使用 JSONPlaceholder API）</h2>
    <!-- 操作按钮 -->
    <el-space style="margin-bottom: 16px">
      <el-button type="primary" @click="exportTable">
        从表格导出 Excel
      </el-button>
      <el-button type="success" @click="exportJson">
        从 JSON 导出 Excel
      </el-button>
      <el-button type="primary" @click="fetchData">刷新数据</el-button>
    </el-space>
    <!-- 表格 -->
    <el-table
        id="my-table"
        :data="tableData"
        border
        v-loading="loading"
        style="width: 100%; margin-bottom: 20px"
    >
      <el-table-column prop="id" label="ID" width="80" align="center"/>
      <el-table-column prop="name" label="姓名" min-width="120" align="center"/>
      <el-table-column prop="username" label="用户名" min-width="120" align="center"/>
      <el-table-column prop="email" label="邮箱" min-width="160" show-overflow-tooltip align="center"/>
      <el-table-column prop="phone" label="电话" min-width="160" show-overflow-tooltip align="center"/>
      <el-table-column prop="website" label="网站" min-width="160" show-overflow-tooltip align="center"/>
    </el-table>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import {ElMessage} from "element-plus";
import {
  export_table_to_excel,
  export_json_to_excel,
} from "@/utils/exportToExcel";

const tableData = ref([]);
const loading = ref(false);

// 获取 API 数据
const fetchData = async () => {
  loading.value = true;
  try {
    const res = await fetch("https://jsonplaceholder.typicode.com/users");
    tableData.value = await res.json();
  } catch (e) {
    ElMessage.error("获取数据失败");
  } finally {
    loading.value = false;
  }
};

onMounted(fetchData);

// 从表格导出
const exportTable = () => {
  if (!tableData.value.length) {
    ElMessage.warning("暂无数据可导出");
    return;
  }
  export_table_to_excel("my-table", "表格导出示例", "表格Sheet");
};

// 从 JSON 导出
const exportJson = () => {
  if (!tableData.value.length) {
    ElMessage.warning("暂无数据可导出");
    return;
  }
  const th = ["ID", "姓名", "用户名", "邮箱", "电话", "网站"];
  const jsonData = tableData.value.map((item) => [
    item.id,
    item.name,
    item.username,
    item.email,
    item.phone,
    item.website,
  ]);
  export_json_to_excel(th, jsonData, "JSON导出示例", "数据Sheet");
};
</script>

<style scoped>
.excel-export {
  padding: 20px;
}
</style>
