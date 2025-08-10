<template>
  <el-button type="success" text
             bg @click="preview">预览
  </el-button>

  <el-button type="primary" text :loading="loading"
             bg @click="download">下载为pdf
  </el-button>
</template>
<script setup>

import axios from "axios";
import {ElMessage} from "element-plus";
import {saveAs} from "file-saver";
import {ref} from "vue";

const loading = ref(false);
const preview = () => {
  axios({
    method: "get",
    url: "http://localhost:5000/pdf/download",
    responseType: "blob"
  })
      .then(response => {
        const blob = new Blob([response.data], {type: "application/pdf"});
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = downloadUrl;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(downloadUrl);
      })
      .catch(error => {
        console.error("预览失败:", error);
        ElMessage.error("PDF预览失败");
      });
};

const download = () => {
  loading.value = true;
  axios.get("http://localhost:5000/pdf/servlet-response-download", {
    responseType: "blob"
  })
      .then(response => {
        const contentDisposition = response.headers["content-disposition"];
        let filename = "download.pdf";
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
        ElMessage.error("PDF下载失败");
      }).finally(() => {
    loading.value = false;
  });
};

</script>
