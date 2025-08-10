<template>
  <div>count : {{ count }}</div>
  <div>别名count : {{ alias }}</div>
  <div>双倍的count : {{ double }}</div>
  <div>count加3 : {{ countPlusN(3) }}</div>
  <div>4倍的count : {{ magicValue }}</div>
  <div>通过 Pinia 安装的插件: {{ counter.secret }}; {{ counter.hello }}; {{ counter.version }}</div>
  <el-button type="success" text
             @click="incr"
             bg>加一
  </el-button>
  <el-button type="success" text
             @click="counter.increment()"
             bg>使用action函数加一
  </el-button>
  <el-button type="success" text
             @click="increment"
             bg>使用mapAction函数加一
  </el-button>
  <el-button type="success" text
             @click="aliasAction"
             bg>使用别名mapAction函数加一
  </el-button>
  <div></div>
</template>


<script>
import {mapState, mapActions} from 'pinia'
import {useCounterStore} from '@/store/OptionsStore'
// Error, 在组件外部调用 useCounterStore(), 此时 Vue 应用尚未初始化, Pinia 也未激活
// const counter = useCounterStore()
export default {
  name: 'OptionsCounterVue',
  data() {
    return {
      counter: useCounterStore()
    }
  },
  computed: {
    // 可以访问组件中的 this.count
    // 与从 store.count 中读取的数据相同
    ...mapState(useCounterStore, ['count']),
    ...mapState(useCounterStore, ['countPlusN']),
    // 与上述相同,但将其注册为 this.myOwnName
    ...mapState(useCounterStore, {
      alias: 'count',
      // 你也可以写一个函数来获得对 store 的访问权
      double: store => store.count * 2,
      // 它可以访问 `this`,但它没有标注类型...
      magicValue(store) {
        return store.count + this.count + this.double
      },
    }),
  },
  mounted() {
    this.counter.$subscribe((mutation, state) => {
      //  此订阅器即便在组件卸载之后仍会被保留
    }, {detached: true})
    // 订阅 action: 用来监听 action 的结果
    const unsubscribe = this.counter.$onAction(
        ({
           name, // action 名称
           store, // store 实例
           args, // 传递给 action 的参数数组
           after, // 在 action 返回或解决后的钩子
           onError, // action 抛出或拒绝的钩子
         }) => {
          // 为这个特定的 action 调用提供一个共享变量
          const startTime = Date.now()
          // 这将在执行 "store "的 action 之前触发.
          console.log(`Start "${name}" with params [${args.join(', ')}].`)

          // 这将在 action 成功并完全运行后触发.
          // 它等待着任何返回的 promise
          after((result) => {
            console.log(
                `Finished "${name}" after ${
                    Date.now() - startTime
                }ms.\nResult: ${result}.`
            )
          })

          // 如果 action 抛出或返回一个拒绝的 promise 这将触发
          onError((error) => {
            console.warn(
                `Failed "${name}" after ${Date.now() - startTime}ms.\nError: ${error}.`
            )
          })
          // 此订阅器即便在组件卸载之后仍会被保留
        }, true
    )
    //手动删除监听器
    unsubscribe()
  },
  methods: {
    // mapAction 不要放入 computed, 会导致 action 被当作计算属性处理, 触发计算属性的重新求值, 从而导致无限递归
    ...mapActions(useCounterStore, ['increment']),
    ...mapActions(useCounterStore, { aliasAction: 'increment' }),
    incr() {
      this.counter.$patch({count: this.counter.count + 1})
    }
  },
}
</script>
