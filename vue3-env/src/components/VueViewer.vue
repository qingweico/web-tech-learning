<template>
  <viewer :images="images" :options="{ title: false }">
    <img v-for="src in images" :key="src" :src="src" class="thumb"  alt=""/>
  </viewer>
</template>

<script setup>
import registerViewer from "@/utils/registerViewer";
registerViewer()
const images = [
  'https://picsum.photos/id/1015/600/400',
  'https://picsum.photos/id/1016/600/400',
  'https://picsum.photos/id/1018/600/400'
]
</script>

<style scoped>
.thumb {
  width: 120px;
  margin: 6px;
  cursor: pointer;
}
</style>
