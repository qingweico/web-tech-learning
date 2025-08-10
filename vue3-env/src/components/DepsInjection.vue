<template>
  <level0-cmp/>
</template>
<script setup>
import { defineComponent } from 'vue'
import { provide } from 'vue'
import Level0Cmp from "@/components/Level0Cmp.vue";

defineComponent({
  name: 'DepsInjection',
})
provide('global', {
  AppCode: 'mNS3KYknBbHXRscD',
  AppName: 'Vue3-Env'
})
</script>
