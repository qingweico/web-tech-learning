<template>
  <div>
    <p>User Center</p>
    <el-button type="success" text
               bg @click=goProfile>Profile</el-button>
    <el-button type="success" text
               bg @click=goSettings>Settings</el-button>
    <el-button type="success" text
               bg @click=forward>forward</el-button>
    <el-button type="success" text
               bg @click=back>back</el-button>
  </div>

</template>
<script setup>

import router from "@/router";

const goProfile = () => {
  router.push({ name: 'profile', query: { plan: 'private' } })
}
const goSettings = () => {
  router.push({ name: 'settings', hash: '#team' , replace: true})
}
const back = () => {
  //router.go(-1)
  window.history.go(-1)
}
const forward = () => {
  //router.go(1)
  window.history.go(1)
}
</script>
