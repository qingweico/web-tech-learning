<template>
  <div>
    <ChildrenPage v-model="ParentNode"/>
  </div>
</template>
<script setup>

import ChildrenPage from "@/components/ChildrenPage.vue";
import {ref} from "vue";

const ParentNode = ref(0)

</script>
