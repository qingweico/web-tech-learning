<template>
  <div>
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart
      :force-fit="true"
      :height="height"
      :data="data"
      :extend="extend"
      :scale="scale"
      :onClick="handleClick"
      :padding="padding"
    >
      <v-tooltip :useHtml="useHtml" :htmlContent="htmlContent" />
      <v-axis />
      <v-legend />

      <v-line position="type*y" color="x" />
      <v-point position="type*y" color="x" :size="4" :v-style="style" :shape="'circle'" />
    </v-chart>
  </div>
</template>

<script>
import { DataSet } from '@antv/data-set'
import { ChartEventMixins } from './mixins/ChartMixins'

export default {
  name: 'LineChartMultid',
  mixins: [ChartEventMixins],
  props: {
    title: {
      type: String,
      default: '',
    },
    useHtml: {
      type: Boolean,
      default: false,
    },
    htmlContent: {
      type: String,
      default: '',
    },
    dataSource: {
      type: Array,
      default: () => [],
    },
    fields: {
      type: Array,
      default: () => [],
    },
    // 别名，需要的格式：[{field:'name',alias:'姓名'}, {field:'sex',alias:'性别'}]
    aliases: {
      type: Array,
      default: () => [],
    },
    height: {
      type: Number,
      default: 400,
    },
    width: {
      type: Number,
      default: 1700,
    },
  },
  data() {
    this.extend = {
      'xAxis.0.axisLabel.rotate': 15,
    }
    this.chartSettings = {
      labelMap: {
        count: '注册月份',
        // 'Order': '下单用户'
      },
      // legendName: {
      //   '访问用户': '访问用户 total: 10000'
      // }
    }
    return {
      scale: [
        {
          dataKey: 'x',
          min: 0,
          max: 1,
        },
      ],
      padding: [20, 'auto', 90, 'auto'],
      style: { stroke: '#fff', lineWidth: 1, show: true },
    }
  },
  computed: {
    data() {
      const dv = new DataSet.View().source(this.dataSource)
      dv.transform({
        type: 'fold',
        fields: this.fields,
        key: 'x',
        value: 'y',
      })
      let rows = dv.rows
      // 替换别名
      console.log(this.aliases)
      rows.forEach((row) => {
        for (let item of this.aliases) {
          if (item.field === row.x) {
            row.x = item.alias
            break
          }
        }
      })
      console.log(rows)
      return rows
    },
  },
}
</script>

<style scoped>
</style>