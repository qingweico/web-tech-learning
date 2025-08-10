<template>
  <div>
    <h3>Home Page</h3>
    <NavBar/>
    <LeftMenu/>
    <!-- 命名视图 其中 name 要与 router#index.js中 router 数组中components 的name对应上 -->
    <router-view/>
    <router-view name="time"/>
    <LogOut/>
    <FooterPage/>
  </div>
</template>
<script setup>
import NavBar from "@/views/NavBar.vue";
import LeftMenu from "@/views/LeftMenu.vue";
import FooterPage from "@/views/FooterPage.vue";
import LogOut from "@/views/LogOut.vue";
</script>
