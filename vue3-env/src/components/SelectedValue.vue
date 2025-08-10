<template>
  <div>Selected: {{ selected }}</div>

  <select v-model="selected">
    <option disabled value="">Please select one</option>
    <option>A</option>
    <option>B</option>
    <option>C</option>
  </select>
  <br/>
  <div>MultipleSelected: {{ selectedMultiple }}</div>

  <select v-model="selectedMultiple" multiple>
    <option>男</option>
    <option>女</option>
    <option>未知</option>
  </select>

  <br/>

  <p>ToggleValue: {{ toggle }}</p>
  <p>InputValue: {{ value }}</p>
  <input
      type="checkbox"
      :value="value"
      v-model="toggle"
      true-value="yes"
      false-value="no"/>

  <br/>
  <p>SelectedValue: {{ pick }}</p>
  <input type="radio" v-model="pick" value="first"/>
  <input type="radio" v-model="pick" value="second"/>

  <br/>

  <p>LazyMsg: {{ msg }}</p>
  <input v-model.lazy="msg"/>
</template>
<script setup>
import {ref} from "vue";

const selected = ref('')
const selectedMultiple = ref([])
const toggle = ref('')
const value = ref('')
const pick = ref('')
const msg = ref('')
</script>
