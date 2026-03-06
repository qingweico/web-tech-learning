<template>
  <div>
    <p>{{ simpleInfo }}</p>
    <a-popover :placement="placement" :trigger="trigger">
      <a>{{ content }}</a>
      <template  slot="content">
        <p>{{ title }}</p>
        <a-row style="width: 300px"  v-for="i in rowsNumber" :key="i">
          <a-col v-for="n in 4" :key="n" :span="6" align="center">
            {{detail[(i-1)*4 + (n -1)]}}
          </a-col>
        </a-row>
      </template>
    </a-popover>
  </div>
</template>

<script>
export default {
  name: "ListParkNumberPopover",
  props:['simpleInfo','placement','trigger','content','title','detail'],
  data(){
    return{
      detailType:'',
      rowsNumber:0
    }
  },
  mounted() {
    let length = Number(this.detail.length)
    this.rowsNumber = Math.ceil(length / 4)
  },
  watch:{
    detailType(){
      return typeof this.detail
    }
  }


}
</script>

<style scoped>

</style>