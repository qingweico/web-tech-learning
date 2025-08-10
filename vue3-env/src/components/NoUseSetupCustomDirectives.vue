<script>
export default {
  name: 'NoUseSetupCustomDirectives',
  directives: {
    // 不带 v-前缀了
    focus: {
      mounted(el) {
        el.focus()
      }
    },

    // 和 attr 透传不同, 父组件中如果有多个root el, 指令不能通过 v-bind="$attrs" 来传递给一个不同的元素
    highlight: {
      mounted(el, binding, vnode) {
        el.style.backgroundColor = binding.value
      }
    }
  }
}
</script>

<template>
  <input v-focus/>
  <div v-highlight="'yellow'">
    This text will have a yellow background.
  </div>

</template>
