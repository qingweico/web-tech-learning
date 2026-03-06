
<template>
  <div>
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart
      :force-fit="true"
      :height="height"
      :data="data"
      :extend="extend"
      :scale="scale"
      :padding="padding"
       >
      <v-tooltip :onChange="change" />
      <v-axis />
      <v-legend />
      <v-line position="type*y" color="x" :label="label"/>
      <v-point position="type*y"  color="x" :size="4" :v-style="style" :shape="'circle'" />
     
    </v-chart>
  </div>
</template>

<script>
import { DataSet } from '@antv/data-set'

export default {
  name: 'LineChartMultidWithCar',
  props: {
    title: {
      type: String,
      default: ''
    },
    dataSource: {
      type: Array
    },
    fields: {
      type: Array
    },
    height: {
      type: Number,
      default: 400
    },
    width: {
      type: Number,
      default: 1700
    }
  },
  data() {

    this.extend = {
      'xAxis.0.axisLabel.rotate': 15
    }
    this.chartSettings = {
      labelMap: {
        count: '注册月份'
        // 'Order': '下单用户'
      }
      // legendName: {
      //   '访问用户': '访问用户 total: 10000'
      // }
    }
    return {

      label:[
          'y',
          {
              position: 'top',
              offset:10
              // textStyle: {
              //     fill: '#fff'
              // }
          }
      ],

         stackBarStyle: {
                stroke: '#fff',
                lineWidth: 1
            },
      //  label:
      //  {
      //   formatter: (val, item) => {
      //      return item.point.item + ': ' + val;
      //   }
      //  },
       scale: [{
        dataKey: 'type',
        min: 0,
      },{
        dataKey: 'enterCount',
        min: 0,
        max: 1,
      },{
         dataKey: 'outCount',
        min: 0,
        max: 1,
      }
      ],
      padding: [20, 'auto', 90, 'auto'],
      style: { stroke: '#fff', lineWidth: 1, show: true },
      guides:{}

    }
  },
  computed: {
    data() {
      this.guides = this.dataSource
      // console.log("this.guides",  this.guides)
      const dv = new DataSet.View().source(this.dataSource)
      dv.transform({
        type: 'fold',
        fields: this.fields,
        key: 'x',
        value: 'y'
      })
      
      return dv.rows
    }
  },
  methods:{

    // label(val, item) {
    //  return item.point.item + ': ' + val;
    // },
    // filter(ev) {
    //   console.log('ene-------',ev.items)
    // },
    change(ev,chart) {
       
      if (ev.items.length ==1) {
        const item = ev.items[0]
          item.value = item.value.replace('辆','')+'辆' 
      } else if (ev.items.length ==2) {
            const item = ev.items[0]
            const item2 = ev.items[1]
             item.value = item.value.replace('辆','')+'辆' 
             item2.value = item2.value.replace('辆','')+'辆';
      }
    
     
      
    }
  }
}
</script>

<style scoped>
</style>