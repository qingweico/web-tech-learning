<script setup>
const vFocus = {
  mounted: (el) => el.focus()
}

const vHighlight = {
  mounted: (el, binding, vnode) => el.style.backgroundColor = binding.value
  // created
  // beforeMount
  // beforeUpdate
  // updated
  // beforeUnmount
  // unmounted
}
</script>

<template>
  <input v-focus />
  <div v-highlight="'yellow'">
    This text will have a yellow background.
  </div>

</template>
