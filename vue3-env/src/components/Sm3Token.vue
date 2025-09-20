<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <h3>Storage 和 Cookie 管理</h3>
      </div>
    </template>

    <el-form label-width="80px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="Key">
            <el-input v-model="storageKey" placeholder="请输入 Key" clearable style="width: 100%"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="Value">
            <el-input v-model="storageValue" placeholder="请输入 Value" clearable style="width: 100%"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <el-button type="primary" @click="saveToStorage">保存到 localStorage</el-button>
            <el-button type="success" @click="loadAllStorage">刷新 localStorage</el-button>
            <el-button type="danger" @click="clearStorage">清空 localStorage</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-divider/>

    <el-form label-width="80px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="Key">
            <el-input v-model="cookieKey" placeholder="请输入 Key" clearable/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="Value">
            <el-input v-model="cookieValue" placeholder="请输入 Value" clearable/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <el-button type="primary" @click="saveCookie">保存 Cookie</el-button>
            <el-button type="success" @click="loadAllCookies">刷新 Cookies</el-button>
            <el-button type="danger" @click="clearCookies">清空 Cookies</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-divider/>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>localStorage 内容 ({{ storageItems.length }} 项)</span>
            </div>
          </template>
          <div v-if="storageItems.length === 0" class="empty-text">
            暂无数据
          </div>
          <div v-else>
            <el-table :data="storageItems" size="small" stripe style="width: 100%">
              <el-table-column prop="key" label="Key" min-width="120" show-overflow-tooltip>
                <template #default="{ row }">
                  <span class="key-text">{{ row.key }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="Value" min-width="180" show-overflow-tooltip>
                <template #default="{ row }">
                  <span class="value-text">{{ row.value }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80" fixed="right">
                <template #default="{ row }">
                  <el-button
                      size="small"
                      type="danger"
                      @click="removeStorageItem(row.key)"
                      class="remove-btn"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Cookies 内容 ({{ cookieItems.length }} 项)</span>
            </div>
          </template>
          <div v-if="cookieItems.length === 0" class="empty-text">
            暂无数据
          </div>
          <div v-else>
            <el-table :data="cookieItems" size="small" stripe style="width: 100%">
              <el-table-column prop="key" label="Key" min-width="120" show-overflow-tooltip>
                <template #default="{ row }">
                  <span class="key-text">{{ row.key }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="Value" min-width="180" show-overflow-tooltip>
                <template #default="{ row }">
                  <span class="value-text">{{ row.value }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80" fixed="right">
                <template #default="{ row }">
                  <el-button
                      size="small"
                      type="danger"
                      @click="removeCookieItem(row.key)"
                      class="remove-btn"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import {ref, onMounted} from "vue";
import {
  setStorageByKey,
  removeStorageByKey,
  setCookieByKey,
  removeCookieByKey,
} from "@/utils/auth";
import {ElMessage, ElNotification} from "element-plus";

const storageKey = ref("");
const storageValue = ref("");
const cookieKey = ref("");
const cookieValue = ref("");

const storageItems = ref([]);
const cookieItems = ref([]);

// 加载所有 localStorage 项
function loadAllStorage() {
  storageItems.value = [];
  for (let i = 0; i < localStorage.length; i++) {
    const key = localStorage.key(i);
    if (key) {
      try {
        const value = localStorage.getItem(key);
        storageItems.value.push({
          key,
          value: value || ""
        });
      } catch (error) {
        console.error(`读取 localStorage 项 ${key} 失败:`, error);
      }
    }
  }
  ElNotification({
    title: "提示",
    message: `刷新 localStorage`,
    type: "success",
  });
}

// 加载所有 Cookies
function loadAllCookies() {
  cookieItems.value = [];
  const cookies = document.cookie.split(";");
  cookies.forEach(cookie => {
    const [key, value] = cookie.split("=").map(item => item.trim());
    if (key) {
      cookieItems.value.push({
        key,
        value: value || ""
      });
    }
  });
  ElNotification({
    title: "提示",
    message: `刷新 Cookies`,
    type: "success",
  });
}

// 保存到 localStorage
function saveToStorage() {
  if (!storageKey.value) {
    ElMessage.warning("请输入 Key");
    return;
  }
  setStorageByKey(storageKey.value, storageValue.value);
  storageKey.value = "";
  storageValue.value = "";
  loadAllStorage();
  ElMessage.success("已保存到 localStorage");
}

// 保存 Cookie
function saveCookie() {
  if (!cookieKey.value) {
    ElMessage.warning("请输入 Key");
    return;
  }
  setCookieByKey(cookieKey.value, cookieValue.value);
  cookieKey.value = "";
  cookieValue.value = "";
  loadAllCookies();
  ElMessage.success("Cookie 已保存");
}

// 删除单个 localStorage 项 (重新加密)
function removeStorageItem(key) {
  removeStorageByKey(key);
  loadAllStorage();
  ElMessage.success(`已删除: ${key}`);
}

// 删除单个 Cookie 项
function removeCookieItem(key) {
  removeCookieByKey(key);
  loadAllCookies();
  ElMessage.success(`已删除: ${key}`);
}

// 清空 localStorage
function clearStorage() {
  if (confirm("确定要清空所有 localStorage 数据吗？")) {
    localStorage.clear();
    loadAllStorage();
    ElMessage.success("localStorage 已清空");
  }
}

// 清空 Cookies
function clearCookies() {
  if (confirm("确定要清空所有 Cookies 吗？")) {
    const cookies = document.cookie.split(";");
    cookies.forEach(cookie => {
      const key = cookie.split("=")[0].trim();
      removeCookieByKey(key);
    });
    loadAllCookies();
    ElMessage.success("Cookies 已清空");
  }
}

// 页面加载时自动读取
onMounted(() => {
  loadAllStorage();
  loadAllCookies();
});
</script>

<style scoped>
.box-card {
  max-width: 1200px;
  margin: 40px auto;
}

.card-header {
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-text {
  text-align: center;
  color: #999;
  padding: 20px;
}

.key-text {
  font-weight: 500;
}

.value-text {
  color: #666;
}

.remove-btn {
  margin-left: 10px;
}

:deep(.el-form-item) {
  margin-bottom: 15px;
}

:deep(.el-table .cell) {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
