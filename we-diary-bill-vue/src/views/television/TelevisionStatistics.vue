<template>
  <a-card :bordered="false">
    <a-row :gutter="24" style="margin-bottom: 16px">
      <a-col :span="6">
        <a-select v-model="timeType" placeholder="请选择时间维度" style="width: 100%" @change="handleTimeTypeChange">
          <a-select-option value="year">年度</a-select-option>
          <a-select-option value="quarter">季度</a-select-option>
          <a-select-option value="month">月度</a-select-option>
          <a-select-option value="week">周度</a-select-option>
        </a-select>
      </a-col>
      <a-col :span="6" v-if="timeType === 'year'">
        <a-input-number v-model="year" placeholder="请输入年度" style="width: 100%" :min="2000" :max="2100" />
      </a-col>
      <a-col :span="6" v-if="timeType === 'quarter'">
        <a-input-number v-model="year" placeholder="请输入年度" style="width: 48%" :min="2000" :max="2100" />
        <a-input-number
          v-model="quarter"
          placeholder="季度"
          style="width: 48%; margin-left: 4%"
          :min="1"
          :max="4"
        />
      </a-col>
      <a-col :span="6" v-if="timeType === 'month'">
        <a-input-number v-model="year" placeholder="请输入年度" style="width: 48%" :min="2000" :max="2100" />
        <a-input-number
          v-model="month"
          placeholder="月份"
          style="width: 48%; margin-left: 4%"
          :min="1"
          :max="12"
        />
      </a-col>
      <a-col :span="6" v-if="timeType === 'week'">
        <a-input-number v-model="year" placeholder="请输入年度" style="width: 48%" :min="2000" :max="2100" />
        <a-input-number v-model="week" placeholder="周数" style="width: 48%; margin-left: 4%" :min="1" :max="53" />
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="loadStatistics">查询</a-button>
      </a-col>
    </a-row>

    <a-row :gutter="24">
      <!-- 评分前十的影视 -->
      <a-col :span="12">
        <a-card title="评分前十的影视" :bordered="false">
          <div ref="top10RatingChart" style="width: 100%; height: 400px"></div>
        </a-card>
      </a-col>

      <!-- 观看状态统计 -->
      <a-col :span="12">
        <a-card title="观看状态统计" :bordered="false">
          <div ref="watchStatusChart" style="width: 100%; height: 400px"></div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px">
      <!-- 观看时长前十的影视 -->
      <a-col :span="12">
        <a-card title="观看时长前十的影视（天）" :bordered="false">
          <div ref="top10DurationChart" style="width: 100%; height: 400px"></div>
        </a-card>
      </a-col>

      <!-- 观影分类前十的影视数量 -->
      <a-col :span="12">
        <a-card title="观影分类前十的影视数量" :bordered="false">
          <div ref="top10CategoryChart" style="width: 100%; height: 400px"></div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="24" style="margin-top: 16px">
      <!-- 时间维度统计 -->
      <a-col :span="24">
        <a-card :title="timeTypeTitle" :bordered="false">
          <div ref="timeStatisticsChart" style="width: 100%; height: 400px"></div>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import {
  getTop10ByRating,
  getWatchStatusStatistics,
  getTop10ByWatchDuration,
  getTop10CategoryStatistics,
  getStatisticsByYear,
  getStatisticsByQuarter,
  getStatisticsByMonth,
  getStatisticsByWeek,
} from '@/api/television'
import * as echarts from 'echarts'

export default {
  name: 'TelevisionStatistics',
  data() {
    return {
      timeType: 'year',
      year: new Date().getFullYear(),
      quarter: null,
      month: null,
      week: null,
      top10RatingChart: null,
      watchStatusChart: null,
      top10DurationChart: null,
      top10CategoryChart: null,
      timeStatisticsChart: null,
    }
  },
  computed: {
    timeTypeTitle() {
      const titles = {
        year: '年度统计（已看完、正在看的影视数量）',
        quarter: '季度统计（已看完、正在看的影视数量）',
        month: '月度统计（已看完、正在看的影视数量）',
        week: '周度统计（已看完、正在看的影视数量）',
      }
      return titles[this.timeType] || '时间维度统计'
    },
  },
  mounted() {
    this.initCharts()
    this.loadAllStatistics()
  },
  beforeDestroy() {
    if (this.top10RatingChart) {
      this.top10RatingChart.dispose()
    }
    if (this.watchStatusChart) {
      this.watchStatusChart.dispose()
    }
    if (this.top10DurationChart) {
      this.top10DurationChart.dispose()
    }
    if (this.top10CategoryChart) {
      this.top10CategoryChart.dispose()
    }
    if (this.timeStatisticsChart) {
      this.timeStatisticsChart.dispose()
    }
  },
  methods: {
    initCharts() {
      this.top10RatingChart = echarts.init(this.$refs.top10RatingChart)
      this.watchStatusChart = echarts.init(this.$refs.watchStatusChart)
      this.top10DurationChart = echarts.init(this.$refs.top10DurationChart)
      this.top10CategoryChart = echarts.init(this.$refs.top10CategoryChart)
      this.timeStatisticsChart = echarts.init(this.$refs.timeStatisticsChart)
    },
    loadAllStatistics() {
      this.loadTop10Rating()
      this.loadWatchStatusStatistics()
      this.loadTop10Duration()
      this.loadTop10Category()
      this.loadTimeStatistics()
    },
    loadTop10Rating() {
      getTop10ByRating().then((res) => {
        if (res.success && res.result) {
          const data = res.result
          const names = data.map((item) => item.name || '未知')
          const ratings = data.map((item) => item.rating || 0)

          const option = {
            color: ['#5470c6'],
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow',
              },
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true,
            },
            xAxis: {
              type: 'category',
              data: names,
              axisLabel: {
                rotate: 45,
                interval: 0,
              },
            },
            yAxis: {
              type: 'value',
              name: '评分',
              max: 10,
            },
            series: [
              {
                name: '评分',
                type: 'bar',
                data: ratings,
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#83bff6' },
                    { offset: 0.5, color: '#188df0' },
                    { offset: 1, color: '#188df0' },
                  ]),
                },
              },
            ],
          }
          this.top10RatingChart.setOption(option)
        }
      })
    },
    loadWatchStatusStatistics() {
      getWatchStatusStatistics().then((res) => {
        if (res.success && res.result) {
          const data = res.result
          const statusMap = { 0: '未开始', 1: '正在追', 2: '已结束' }
          const pieData = data.map((item) => ({
            value: item.count,
            name: statusMap[item.status] || '未知',
          }))

          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)',
            },
            legend: {
              orient: 'vertical',
              left: 'left',
            },
            series: [
              {
                name: '观看状态',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                  borderRadius: 10,
                  borderColor: '#fff',
                  borderWidth: 2,
                },
                label: {
                  show: false,
                  position: 'center',
                },
                emphasis: {
                  label: {
                    show: true,
                    fontSize: '30',
                    fontWeight: 'bold',
                  },
                },
                labelLine: {
                  show: false,
                },
                data: pieData,
                color: ['#91cc75', '#fac858', '#ee6666'],
              },
            ],
          }
          this.watchStatusChart.setOption(option)
        }
      })
    },
    loadTop10Duration() {
      getTop10ByWatchDuration().then((res) => {
        if (res.success && res.result) {
          const data = res.result
          const names = data.map((item) => item.name || '未知')
          const durations = data.map((item) => item.duration_days || 0)

          const option = {
            color: ['#91cc75'],
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow',
              },
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true,
            },
            xAxis: {
              type: 'category',
              data: names,
              axisLabel: {
                rotate: 45,
                interval: 0,
              },
            },
            yAxis: {
              type: 'value',
              name: '观看时长（天）',
            },
            series: [
              {
                name: '观看时长',
                type: 'bar',
                data: durations,
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#91cc75' },
                    { offset: 1, color: '#73a373' },
                  ]),
                },
              },
            ],
          }
          this.top10DurationChart.setOption(option)
        }
      })
    },
    loadTop10Category() {
      getTop10CategoryStatistics().then((res) => {
        if (res.success && res.result) {
          const data = res.result
          const names = data.map((item) => item.category_name || '未知')
          const counts = data.map((item) => item.count || 0)

          const option = {
            color: ['#fac858'],
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow',
              },
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true,
            },
            xAxis: {
              type: 'category',
              data: names,
              axisLabel: {
                rotate: 45,
                interval: 0,
              },
            },
            yAxis: {
              type: 'value',
              name: '数量',
            },
            series: [
              {
                name: '影视数量',
                type: 'bar',
                data: counts,
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#fac858' },
                    { offset: 1, color: '#d4a853' },
                  ]),
                },
              },
            ],
          }
          this.top10CategoryChart.setOption(option)
        }
      })
    },
    loadTimeStatistics() {
      let promise
      const params = {}
      if (this.timeType === 'year') {
        promise = getStatisticsByYear({ year: this.year })
      } else if (this.timeType === 'quarter') {
        promise = getStatisticsByQuarter({ year: this.year, quarter: this.quarter })
      } else if (this.timeType === 'month') {
        promise = getStatisticsByMonth({ year: this.year, month: this.month })
      } else if (this.timeType === 'week') {
        promise = getStatisticsByWeek({ year: this.year, week: this.week })
      }

      if (promise) {
        promise.then((res) => {
          if (res.success && res.result) {
            const data = res.result
            let xAxisData = []
            const finishedData = []
            const watchingData = []

            if (this.timeType === 'year') {
              xAxisData = data.map((item) => `${item.year}年`)
            } else if (this.timeType === 'quarter') {
              xAxisData = data.map((item) => `${item.year}年Q${item.quarter}`)
            } else if (this.timeType === 'month') {
              xAxisData = data.map((item) => item.year_month)
            } else if (this.timeType === 'week') {
              xAxisData = data.map((item) => `${item.year}年第${item.week}周`)
            }

            data.forEach((item) => {
              finishedData.push(item.finished_count || 0)
              watchingData.push(item.watching_count || 0)
            })

            const option = {
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'cross',
                },
              },
              legend: {
                data: ['已看完', '正在看'],
              },
              grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true,
              },
              xAxis: {
                type: 'category',
                boundaryGap: false,
                data: xAxisData,
                axisLabel: {
                  rotate: 45,
                },
              },
              yAxis: {
                type: 'value',
                name: '数量',
              },
              series: [
                {
                  name: '已看完',
                  type: 'line',
                  stack: 'Total',
                  smooth: true,
                  data: finishedData,
                  itemStyle: {
                    color: '#91cc75',
                  },
                  areaStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      { offset: 0, color: 'rgba(145, 204, 117, 0.3)' },
                      { offset: 1, color: 'rgba(145, 204, 117, 0.1)' },
                    ]),
                  },
                },
                {
                  name: '正在看',
                  type: 'line',
                  stack: 'Total',
                  smooth: true,
                  data: watchingData,
                  itemStyle: {
                    color: '#fac858',
                  },
                  areaStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      { offset: 0, color: 'rgba(250, 200, 88, 0.3)' },
                      { offset: 1, color: 'rgba(250, 200, 88, 0.1)' },
                    ]),
                  },
                },
              ],
            }
            this.timeStatisticsChart.setOption(option)
          }
        })
      }
    },
    handleTimeTypeChange() {
      this.quarter = null
      this.month = null
      this.week = null
    },
    loadStatistics() {
      this.loadTimeStatistics()
    },
  },
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>







