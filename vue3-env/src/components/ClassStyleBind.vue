<template>
  <div
      :class="{ active: isActive, 'text-danger': hasError }"
  >这是一段话</div>
  <el-button type="success" text
             bg @click="isActive = !isActive">active</el-button>
  <el-button type="success" text
             bg @click="hasError = !hasError">hasError</el-button>

  <br/>

  <div
      :class="classObject"
  >这是另外一段话</div>

  <br/>

  <div
      :class="computedClassObject"
  >这是第二段话</div>

  <br/>

  <div
      :class="[activeClass, errorClass]"
  >这是第三段话</div>  <br/>

  <div
      :class="[[{[activeClass]: isActive}], errorClass]"
  >这是第四段话</div>

</template>

<script setup>
import {computed, reactive, ref} from "vue";
/*Style 差不多*/
const isActive = ref(true)
const hasError = ref(false)
const classObject = reactive({
  active: true,
  'text-danger': true
})
const computedClassObject = computed(() => ({
  active: true,
  'text-danger': true
}))


const activeClass = ref('active')
const errorClass = ref('text-danger')


</script>
<style>
.active {
  font-size: 30pt;
}
.text-danger {
  color: red;
}
</style>
