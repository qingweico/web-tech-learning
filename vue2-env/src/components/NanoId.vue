<template>
  <div>
    <ul
      v-for="e in idList"
      :key="e"
    >
      {{ e }}
    </ul>
  </div>
</template>

<script>
import {nanoid} from 'nanoid'

export default {
  name: 'NanoId',
  data() {
    return {
      idList: []
    }
  },
  created() {
    for (let i = 0; i < 10; i++) {
      this.idList.push(nanoid())
    }
  },
  methods: {}
}
</script>
<style>

</style>
