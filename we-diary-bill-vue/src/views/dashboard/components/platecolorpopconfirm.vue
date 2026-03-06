<template>
  <a-popconfirm
    placement="bottom"
    :ok-text="false"
    :cancel-text="false"
    :getPopupContainer="
      (triggerNode) => {
        return triggerNode.parentNode || document.body
      }
    "
  >
    <template #title>
      <table>
        <tr>
          <td class="colorbox" data-color="0" @click="ckcolorbox">
            蓝色
            <div class="ck" :data-ck="colorType == 0"></div>
          </td>
          <td class="colorbox" data-color="1" @click="ckcolorbox">
            黄色
            <div class="ck" :data-ck="colorType == 1"></div>
          </td>
          <td class="colorbox" data-color="2" @click="ckcolorbox">
            白色
            <div class="ck" :data-ck="colorType == 2"></div>
          </td>
        </tr>
        <tr>
          <td class="colorbox" data-color="3" @click="ckcolorbox">
            绿色
            <div class="ck" :data-ck="colorType == 3"></div>
          </td>
          <td class="colorbox" data-color="4" @click="ckcolorbox">
            黄绿
            <div class="ck" :data-ck="colorType == 4"></div>
          </td>
          <td class="colorbox" data-color="5" @click="ckcolorbox">
            黑色
            <div class="ck" :data-ck="colorType == 5"></div>
          </td>
        </tr>
      </table>
    </template>

    <slot></slot>
  </a-popconfirm>
</template>

<script>
export default {
  name: 'platecolorpopconfirm',
  props: {
    colorType: {
      type: Number,
      value: 0,
    },
  },
  data() {
    return {}
  },
  created() {},
  mounted() {},
  methods: {
    ckcolorbox(event) {
      this.$emit('altercolor', event.target.dataset.color)
    },
  },
  watch: {},
}
</script>

<style lang="less" scoped>
</style>