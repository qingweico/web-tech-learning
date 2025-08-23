<template>
  <el-button type="primary" text :loading="loading"
             bg @click="download">下载为word
  </el-button>
</template>
<script setup>

import axios from "axios";
import {ElMessage} from "element-plus";
import {saveAs} from "file-saver";
import {ref} from "vue";

const loading = ref(false);


const download = () => {
  loading.value = true;
  axios.get("http://localhost:5000/word/download", {
    responseType: "blob"
  })
      .then(response => {
        const contentDisposition = response.headers["content-disposition"];
        let filename = "download.docx";
        if (contentDisposition) {
          const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
          const matches = filenameRegex.exec(contentDisposition);
          if (matches != null && matches[1]) {
            filename = matches[1].replace(/['"]/g, "");
          }
        }
        saveAs(response.data, filename);
        loading.value = false;
      })
      .catch(error => {
        console.error("下载失败:", error);
        ElMessage.error("Word下载失败");
      }).finally(() => {
    loading.value = false;
  });
};

</script>
