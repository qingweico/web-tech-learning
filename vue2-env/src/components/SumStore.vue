<template>
  <div>
    <h2 style="line-height: 30px;padding: 20px">
      Sum : {{ sum }}
    </h2>
    <h4 style="line-height: 30px;padding: 20px">
      x10 : {{ x10 }}
    </h4>
    <div class="container">
      <el-select v-model="selected">
        <el-option
          v-for="item in options"
          :key="item.id"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <div style="margin-left: 20px">
        <el-button @click="plus(selected)">
          +
        </el-button>
        <el-button @click="minus(selected)">
          -
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {mapState, mapGetters, mapActions, mapMutations} from 'vuex'

export default {
  name: 'SumStore',
  data() {
    return {
      selected: 1,
      options: [{
        id: '1',
        value: 1,
        label: '1'
      }, {
        id: '2',
        value: 2,
        label: '2'
      }, {
        id: '3',
        value: 3,
        label: '3'
      }]
    }
  },
  computed: {
    // 映射 state 中的数据为计算属性
    // key 为要展示的(自定义的)属性, value为state中的key
    ...mapState('sum', {
      'sum': 'sum'
    }),
    // // 映射 getters 中的数据为计算属性
    ...mapGetters('sum', {
      'x10': 'x10'
    })
  },
  methods: {
    // plus() {
    //   this.$store.dispatch('sum/plus', this.selected)
    // },
    ...mapActions('sum', {plus: 'plus'}),
    ...mapActions('sum', {minus: 'plus'}),
    ...mapMutations('sum', {plus: 'PLUS'}),
    ...mapMutations('sum', {minus: 'MINUS'}),
    // minus() {
    //   this.$store.dispatch('sum/minus', this.selected)
    // }

  }
}
</script>
<style scoped>

.container {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 20px;
}
</style>
