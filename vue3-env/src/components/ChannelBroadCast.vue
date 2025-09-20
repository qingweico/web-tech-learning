<template>
  <el-container style="padding: 20px">
    <el-card class="box-card" shadow="hover" style="width: 600px;">
      <template #header>
        <span>Broadcast / ChannelBroadCast 测试</span>
      </template>

      <el-form :inline="true" label-width="80px">
        <el-form-item label="消息内容">
          <el-input v-model="msg" placeholder="请输入消息" clearable style="width: 300px;" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="sendLocal">发送 Broadcast</el-button>
          <el-button type="success" @click="sendTab">发送 ChannelBroadCast</el-button>
        </el-form-item>
      </el-form>

      <el-divider />

      <div>
        <h3>接收到的消息</h3>
        <el-scrollbar height="200px">
          <div v-for="(m, index) in messages" :key="index" style="margin-bottom: 6px;">
            <el-tag :type="m.type" effect="light">
              [{{ m.from }}] {{ m.text }}
            </el-tag>
          </div>
        </el-scrollbar>
      </div>
    </el-card>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import Broadcast, { ChannelBroadCast } from "@/utils/broadcast";

const msg = ref("");
const messages = ref([]);

// 发送本地 Broadcast
function sendLocal() {
  if (!msg.value) return;
  Broadcast.emit("local-msg", msg.value);
  msg.value = "";
}

// 发送跨 tab 的 ChannelBroadCast
function sendTab() {
  if (!msg.value) return;
  ChannelBroadCast.emit("tab-msg", msg.value);
  msg.value = "";
}

onMounted(() => {
  // 监听本地消息
  Broadcast.on("local-msg", (data) => {
    messages.value.push({
      from: "Broadcast",
      text: data,
      type: "info",
    });
  });

  // 监听跨 tab 消息
  ChannelBroadCast.on("tab-msg", (data) => {
    messages.value.push({
      from: "ChannelBroadCast",
      text: data,
      type: "success",
    });
  });
});
</script>

<style scoped>
.box-card {
  border-radius: 12px;
}
</style>
