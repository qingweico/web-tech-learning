<template>
  <div>
    <p>AppCode: {{ global.AppCode }}</p>
    <p>AppName: {{ global.AppName }}</p>
  </div>
</template>
<script setup>
import {inject} from 'vue'

const global = inject('global', {}, true)
</script>
