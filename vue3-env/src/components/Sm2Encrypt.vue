<template>
  <div style="width: 300px">
    <el-form @submit.prevent="handleSubmit">
      <el-form-item label="明文密码">
        <el-input
            v-model="password"
            ref="pwdRef"
            placeholder="请输入密码"
        >
          <template #append>
            <el-button type="primary" @click="handleSubmit">提交</el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="密文密码">
        <el-input type="textarea" v-model="encryptData" :autosize="{maxRows: 10, minRows: 10}"></el-input>
      </el-form-item>
      <el-form-item v-if="decodedPassword" label="解密密码">
        {{ decodedPassword }}
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import {ref} from "vue";
import axios from "axios";
import {sm2} from "sm-crypto";
import {ElMessage} from "element-plus";

const password = ref("");
const decodedPassword = ref("");
const encryptData = ref("");
const pwdRef = ref(null);
const publicKey = "042d36dfdf646d71ff4380a87c653448905f02a5d6642b29c87a3fa5d63265d56c6152da1b8aa486a1b220d4138caf83ba4b69bd7da96da16b144386ff96ab57ca";

const handleSubmit = async () => {
  if (!password.value) {
    pwdRef.value.focus();
    return;
  }
  encryptData.value = sm2.doEncrypt(password.value, publicKey, 0);
  try {
    const response = await axios.post("/api/sm2/decode", {
      content: encryptData.value
    });
    decodedPassword.value = response.data.data;
    ElMessage.success("解密成功");
  } catch (err) {
    ElMessage.error(`${err.message}`);
    decodedPassword.value = "";
  }
};
</script>
