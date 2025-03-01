<template>
  <div id="app">
    <fingerprint />
    <UaParserService/>
  </div>
</template>

<script>
import Fingerprint from "@/views/Fingerprint.vue";
import UaParserService from "@/views/UaParserService.vue";

export default {
  name: 'App',
  components: {
    UaParserService,
    Fingerprint
    }
}
</script>

<style>


</style>
