<template>
  <selectable-calendar
      v-model="selectedDates"
      :range="['2025-09-10', '2025-09-12']"
      out-range-title="只能选择 9 月 10 日到 12 日"
      format="YYYY-MM-DD"/>
  <p>已选日期 {{ selectedDates }}</p>
</template>

<script setup>
import {ref} from "vue";
import SelectableCalendar from "@/components/SelectableCalendar.vue";
const selectedDates = ref([]);
</script>
