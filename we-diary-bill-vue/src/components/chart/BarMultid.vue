<!--
 * @Author: your name
 * @Date: 2019-08-01 13:46:00
 * @LastEditTime: 2020-05-20 11:38:46
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \ant-design-vue-jdd\src\components\chart\BarMultid.vue
 -->
<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :forceFit="true" :height="height" :data="data">
      <v-tooltip  :onChange="change" />
      <v-axis />
      <v-legend />
      <v-bar position="x*y" color="type" :adjust="adjust"  :label="label" />
       
      
    </v-chart>
  </div>
</template>

<script>
  import { DataSet } from '@antv/data-set'
  const label = {
  textStyle: {
    fill: '#aaaaaa'
  }
}
 const barLabel = [ {
   offset: 10,
  textStyle: {
     fill: '#595959',
    fontSize: 12
   }
 }];
  export default {
    name: 'BarMultid',
    props: {
      title: {
        type: String,
        default: ''
      },
      dataSource:{
        type: Array,
        default: () => [
          { type: 'Jeecg', 'Jan.': 18.9, 'Feb.': 28.8, 'Mar.': 39.3, 'Apr.': 81.4, 'May': 47, 'Jun.': 20.3, 'Jul.': 24, 'Aug.': 35.6 },
          { type: 'Jeebt', 'Jan.': 12.4, 'Feb.': 23.2, 'Mar.': 34.5, 'Apr.': 99.7, 'May': 52.6, 'Jun.': 35.5, 'Jul.': 37.4, 'Aug.': 42.4 },
          { type: 'brade', 'Jan.': 12.4, 'Feb.': 23.2, 'Mar.': 34.5, 'Apr.': 99.7, 'May': 52.6, 'Jun.': 35.5, 'Jul.': 37.4, 'Aug.': 42.4 }
        ]
      },
      fields:{
        type: Array,
        default: () => ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May', 'Jun.', 'Jul.', 'Aug.']
      },
      height: {
        type: Number,
        default: 254
      }
    },
    data() {
      return {
        barLabel,
      label:[
          'y',
          {
              position: 'top',
              offset:8
              // textStyle: {
              //     fill: '#fff'
              // }
          }
      ],
        //   barLabel : ['y', {
        //     offset: 10,
        //     textStyle: {
        //       fill: '#595959',
        //       fontSize: 12
        //     }
        // }],
        adjust: [{
          type: 'dodge',
          marginRatio: 1 / 32
        }]
      }
    },
    computed: {
      data() {
        const dv = new DataSet.View().source(this.dataSource)
        dv.transform({
          type: 'fold',
          fields: this.fields,
          key: 'x',
          value: 'y'
        })

        // bar 使用不了 - 和 / 所以替换下
        return dv.rows.map(row => {
          row.x = row.x.replace(/[-/]/g, '_')
          return row
        })

      }
    },
   methods:{
    change(ev,chart) {
      const item = ev.items[0]
      const item2 = ev.items[1]
      const item3 = ev.items[2]
      item.value = item.value.replace('条','')+'条' 
      item2.value = item2.value.replace('辆','')+'辆'
      item3.value = item3.value.replace('元','')+'元'
    }
  }
  }
</script>

<style scoped>

</style>