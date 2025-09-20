<template>
  <div>
    <el-table :data="[]" style="width: 100%">
    </el-table>

    <MyPagination
        :total="total"
        v-model:page="page"
        v-model:limit="limit"
        @pagination="handlePagination"
    >
      <template #left>
        <span>共 {{ total }} 条数据</span>
      </template>
    </MyPagination>
  </div>
</template>

<script setup>
import {ref} from "vue";
import MyPagination from "@/components/MyPagination.vue";
import { showFullScreenLoading } from "@/utils";
const total = ref(200);
const page = ref(1);
const limit = ref(20);

const handlePagination = ({page: newPage, limit: newLimit}) => {
  console.log("分页变化:", newPage, newLimit);
  const loading = showFullScreenLoading("正在加载第 " + newPage + " 页...");
  page.value = newPage;
  limit.value = newLimit;
  setTimeout(() => {
    loading.close();
  }, 460)
};
</script>

