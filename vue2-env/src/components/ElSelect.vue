<template>
  <div>
    <el-select
      v-model="select"
      multiple
      size="mini"
      placeholder="请选择"
    >
      <el-option
        v-for="item in options"
        :key="item.id"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
  </div>
</template>

<script>
export default {
  data() {
    return {
      options: [{
        id: '01',
        value: '01',
        label: '黄金糕'
      }, {
        id: '02',
        value: '02',
        label: '双皮奶'
      }, {
        id: '03',
        value: '03',
        label: '蚵仔煎'
      }, {
        id: '04',
        value: '04',
        label: '龙须面'
      }, {
        id: '05',
        value: '05',
        label: '北京烤鸭'
      }],
      select: ['01']
    }
  }
}
</script>
