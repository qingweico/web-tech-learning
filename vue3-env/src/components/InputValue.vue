<template>
  <p>Value: {{ inputValue }}</p>
  <input
      :value="inputValue"
      @input="handleInput"
  />
</template>

<script setup>
import { ref } from 'vue';

const inputValue = ref('');

const handleInput = (event) => {
  inputValue.value = event.target.value;
};
</script>
