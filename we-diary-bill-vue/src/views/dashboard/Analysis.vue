<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" title="系统概览" class="overview">
      <a-row ref="Overview" :gutter="[48, 48]" type="flex">
        <a-col flex="auto">
          <div class="overviewItem overviewItem2">
            <h3>今日支出</h3>
            <div>
              <b>{{ todayExpense }}</b>
              <span style="position: absolute; margin-top: 19px">元</span>
            </div>
          </div>
        </a-col>
        <a-col flex="auto">
          <div class="overviewItem overviewItem3">
            <h3>今日收入</h3>
            <div>
              <b>{{ todayIncome }}</b>
              <span style="position: absolute; margin-top: 19px">元</span>
            </div>
          </div>
        </a-col>
        <a-col flex="auto">
          <div class="overviewItem overviewItem4">
            <h3>总体支出</h3>
            <div>
              <b>{{ totalExpense }}</b>
              <span style="position: absolute; margin-top: 19px">元</span>
            </div>
          </div>
        </a-col>
        <a-col flex="auto">
          <div class="overviewItem overviewItem1">
            <h3>总体收入</h3>
            <div>
              <b>{{ totalIncome }}</b>
              <span style="position: absolute; margin-top: 19px">元</span>
            </div>
          </div>
        </a-col>
      </a-row>
    </a-card>
    <!-- 近十五天收入分析和支出分析 - 同一行 -->
    <a-row :gutter="24">
      <a-col :span="12">
        <a-card :bordered="false" title="近十五天收入分析">
          <div class="chartWrap" id="incomeChartWrap" style="height: 400px"></div>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card :bordered="false" title="近十五天支出分析">
          <div class="chartWrap" id="expenseChartWrap" style="height: 400px"></div>
        </a-card>
      </a-col>
    </a-row>
    <!-- 按分类统计收入和支出 - 同一行 -->
    <a-row :gutter="24">
      <a-col :span="12">
        <a-card :bordered="false" title="收入分类统计">
          <div class="chartWrap" id="incomeCategoryChartWrap" style="height: 400px"></div>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card :bordered="false" title="支出分类统计">
          <div class="chartWrap" id="expenseCategoryChartWrap" style="height: 400px"></div>
        </a-card>
      </a-col>
    </a-row>
    <!-- 按支付方式统计收入和支出 - 同一行 -->
    <a-row :gutter="24">
      <a-col :span="12">
        <a-card :bordered="false" title="收入支付方式统计">
          <div class="chartWrap" id="incomePaymentChartWrap" style="height: 400px"></div>
    </a-card>
      </a-col>
      <a-col :span="12">
        <a-card :bordered="false" title="支出支付方式统计">
          <div class="chartWrap" id="expensePaymentChartWrap" style="height: 400px"></div>
    </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :span="12">
        <a-card title="收入记录">
          <a slot="extra" @click="goToRecordList(1)">更多</a>
          <a-table :pagination="false" :columns="columns_income" :data-source="data_income" rowKey="id"> </a-table>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="支出记录">
          <a slot="extra" @click="goToRecordList(2)">更多</a>
          <a-table :pagination="false" :columns="columns_expense" :data-source="data_expense" rowKey="id"> </a-table>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { postAction } from '@api/manage'
import { ajaxGetDictItems } from '@api/api'
import { getCategoryListByType } from '@/api/bill'

const columns_income = [
  {
    title: '分类',
    dataIndex: 'categoryName',
    width: 100,
  },
  {
    title: '金额',
    dataIndex: 'amount',
    width: 100,
    customRender: (text) => {
      return '¥' + parseFloat(text).toFixed(2)
    },
  },
  {
    title: '支付方式',
    dataIndex: 'payment',
    width: 100,
  },
  {
    title: '时间',
    dataIndex: 'billTime',
    width: 150,
  },
]

const columns_expense = [
  {
    title: '分类',
    dataIndex: 'categoryName',
    width: 100,
  },
  {
    title: '金额',
    dataIndex: 'amount',
    width: 100,
    customRender: (text) => {
      return '¥' + parseFloat(text).toFixed(2)
    },
  },
  {
    title: '支付方式',
    dataIndex: 'payment',
    width: 100,
  },
  {
    title: '时间',
    dataIndex: 'billTime',
    width: 150,
  },
]

export default {
  name: 'Analysis',
  components: {},
  data() {
    return {
      todayIncome: '0.00',
      todayExpense: '0.00',
      totalIncome: '0.00',
      totalExpense: '0.00',
      data_income: [],
      data_expense: [],
      columns_income,
      columns_expense,
      url: {
        BillAnalysisData: '/dashboard/analysis/BillAnalysisData',
      },
      incomeChart: null,
      expenseChart: null,
      incomeCategoryChart: null,
      expenseCategoryChart: null,
      incomePaymentChart: null,
      expensePaymentChart: null,
      categoryList: [],
      paymentList: [],
      resizeTimer: null,
    }
  },
  mounted() {
    this.loadBillAnalysisData()
  },
  beforeDestroy() {
    if (this.incomeChart) {
      this.incomeChart.dispose()
    }
    if (this.expenseChart) {
      this.expenseChart.dispose()
    }
    if (this.incomeCategoryChart) {
      this.incomeCategoryChart.dispose()
    }
    if (this.expenseCategoryChart) {
      this.expenseCategoryChart.dispose()
    }
    if (this.incomePaymentChart) {
      this.incomePaymentChart.dispose()
    }
    if (this.expensePaymentChart) {
      this.expensePaymentChart.dispose()
    }
    if (this.resizeTimer) {
      clearTimeout(this.resizeTimer)
    }
  },
  methods: {
    loadBillAnalysisData() {
      postAction(this.url.BillAnalysisData, {}).then((res) => {
        if (res.success) {
          const data = res.result
          // 设置统计数据
          this.todayIncome = this.formatAmount(data.todayIncome)
          this.todayExpense = this.formatAmount(data.todayExpense)
          this.totalIncome = this.formatAmount(data.totalIncome)
          this.totalExpense = this.formatAmount(data.totalExpense)

          // 绘制图表
          this.$nextTick(() => {
            this.renderIncomeChart(data.incomeAnalysis)
            this.renderExpenseChart(data.expenseAnalysis)
            // 加载分类和支付方式字典后渲染饼图和处理记录
            this.loadCategoryAndPaymentData().then(() => {
              this.renderIncomeCategoryChart(data.incomeCategoryStats)
              this.renderExpenseCategoryChart(data.expenseCategoryStats)
              this.renderIncomePaymentChart(data.incomePaymentStats)
              this.renderExpensePaymentChart(data.expensePaymentStats)
              // 处理最近记录（需要分类和支付方式字典）
              this.processRecentRecords(data.recentIncomeRecords, data.recentExpenseRecords)
            })
          })
        } else {
          this.$message.error(res.message || '加载数据失败')
        }
      })
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toFixed(2)
    },
    processRecentRecords(incomeRecords, expenseRecords) {
      // 处理收入记录
      this.data_income = (incomeRecords || []).map((record, index) => {
        const category = this.categoryList.find((c) => c.id === record.categoryId)
        const payment = this.paymentList.find((p) => p.value === record.payment)
        return {
          id: record.id || index,
          categoryName: category ? category.categoryName : '-',
          amount: record.amount,
          payment: payment ? payment.text : record.payment || '-',
          billTime: this.formatDateTime(record.billTime),
        }
      })

      // 处理支出记录
      this.data_expense = (expenseRecords || []).map((record, index) => {
        const category = this.categoryList.find((c) => c.id === record.categoryId)
        const payment = this.paymentList.find((p) => p.value === record.payment)
        return {
          id: record.id || index,
          categoryName: category ? category.categoryName : '-',
          amount: record.amount,
          payment: payment ? payment.text : record.payment || '-',
          billTime: this.formatDateTime(record.billTime),
        }
      })
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      const date = new Date(dateTime)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}`
    },
    renderIncomeChart(analysisData) {
      const ele = document.getElementById('incomeChartWrap')
      if (!ele) {
        return
      }

      if (this.incomeChart) {
        this.incomeChart.dispose()
      }
      this.incomeChart = echarts.init(ele)

      // 处理数据：按日期汇总金额
      const dateMap = new Map()
      const categoryMap = new Map()
      const paymentMap = new Map()

      ;(analysisData || []).forEach((item) => {
        const date = item.date
        const category = item.categoryName || '其他'
        const payment = item.payment || '其他'
        const amount = parseFloat(item.amount || 0)

        // 按日期汇总
        if (!dateMap.has(date)) {
          dateMap.set(date, 0)
        }
        dateMap.set(date, dateMap.get(date) + amount)

        // 统计分类
        if (!categoryMap.has(category)) {
          categoryMap.set(category, 0)
        }
        categoryMap.set(category, categoryMap.get(category) + amount)

        // 统计支付方式
        if (!paymentMap.has(payment)) {
          paymentMap.set(payment, 0)
        }
        paymentMap.set(payment, paymentMap.get(payment) + amount)
      })

      // 生成日期列表（近15天）
      const dates = []
      const amounts = []
      for (let i = 14; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        const dateStr = this.formatDate(date)
        dates.push(dateStr)
        amounts.push(dateMap.get(dateStr) || 0)
      }

      // 计算总金额
      let totalAmount = 0
      analysisData.forEach((item) => {
        totalAmount += parseFloat(item.amount || 0)
      })

      // 获取前5个分类和支付方式用于图例
      const topCategories = Array.from(categoryMap.entries())
        .sort((a, b) => b[1] - a[1])
        .slice(0, 5)
        .map((item) => item[0])
      const topPayments = Array.from(paymentMap.entries())
        .sort((a, b) => b[1] - a[1])
        .slice(0, 5)
        .map((item) => item[0])

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
          formatter: function (params) {
            let result = params[0].axisValue + '<br/>'
            let total = 0
            params.forEach((param) => {
              if (param.value > 0) {
                result += param.marker + param.seriesName + ': ¥' + parseFloat(param.value).toFixed(2) + '<br/>'
                total += parseFloat(param.value)
              }
            })
            result += '<b>合计: ¥' + total.toFixed(2) + '</b>'
            return result
          },
        },
        legend: {
          data: ['收入金额'],
          top: 10,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 45,
            interval: 0,
          },
        },
        yAxis: {
          type: 'value',
            axisLabel: {
              formatter: (value) => {
              return '¥' + value
            },
          },
        },
        title: {
          text: '总收入：¥' + totalAmount.toFixed(2),
          left: 'center',
          top: 30,
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold',
            color: '#52c41a',
          },
        },
        series: [
          {
            name: '收入金额',
            type: 'line',
            smooth: true,
            data: amounts,
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: 'rgba(82, 196, 26, 0.3)',
                  },
                  {
                    offset: 1,
                    color: 'rgba(82, 196, 26, 0.1)',
                  },
                ],
              },
            },
            lineStyle: {
              color: '#52c41a',
              width: 2,
            },
            itemStyle: {
              color: '#52c41a',
            },
          },
        ],
      }

      this.incomeChart.setOption(option)

      // 窗口大小改变时重新调整图表
      this.setupResizeHandler()
    },
    renderExpenseChart(analysisData) {
      const ele = document.getElementById('expenseChartWrap')
      if (!ele) {
        return
      }

      if (this.expenseChart) {
        this.expenseChart.dispose()
      }
      this.expenseChart = echarts.init(ele)

      // 处理数据：按日期汇总金额
      const dateMap = new Map()
      const categoryMap = new Map()
      const paymentMap = new Map()

      ;(analysisData || []).forEach((item) => {
        const date = item.date
        const category = item.categoryName || '其他'
        const payment = item.payment || '其他'
        const amount = parseFloat(item.amount || 0)

        // 按日期汇总
        if (!dateMap.has(date)) {
          dateMap.set(date, 0)
        }
        dateMap.set(date, dateMap.get(date) + amount)

        // 统计分类
        if (!categoryMap.has(category)) {
          categoryMap.set(category, 0)
        }
        categoryMap.set(category, categoryMap.get(category) + amount)

        // 统计支付方式
        if (!paymentMap.has(payment)) {
          paymentMap.set(payment, 0)
        }
        paymentMap.set(payment, paymentMap.get(payment) + amount)
      })

      // 生成日期列表（近15天）
      const dates = []
      const amounts = []
      for (let i = 14; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        const dateStr = this.formatDate(date)
        dates.push(dateStr)
        amounts.push(dateMap.get(dateStr) || 0)
      }

      // 计算总金额
      let totalAmount = 0
      analysisData.forEach((item) => {
        totalAmount += parseFloat(item.amount || 0)
      })

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
          formatter: function (params) {
            let result = params[0].axisValue + '<br/>'
            let total = 0
            params.forEach((param) => {
              if (param.value > 0) {
                result += param.marker + param.seriesName + ': ¥' + parseFloat(param.value).toFixed(2) + '<br/>'
                total += parseFloat(param.value)
              }
            })
            result += '<b>合计: ¥' + total.toFixed(2) + '</b>'
            return result
          },
        },
        legend: {
          data: ['支出金额'],
          top: 10,
          },
          grid: {
          left: '3%',
          right: '4%',
          bottom: '10%',
          containLabel: true,
          },
          xAxis: {
          type: 'category',
          data: dates,
            axisLabel: {
            rotate: 45,
              interval: 0,
          },
        },
        yAxis: {
          type: 'value',
              axisLabel: {
                formatter: (value) => {
              return '¥' + value
            },
          },
        },
        title: {
          text: '总支出：¥' + totalAmount.toFixed(2),
          left: 'center',
          top: 30,
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold',
            color: '#ff4d4f',
          },
        },
        series: [
          {
            name: '支出金额',
              type: 'line',
              smooth: true,
            data: amounts,
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                    color: 'rgba(255, 77, 79, 0.3)',
                    },
                    {
                      offset: 1,
                    color: 'rgba(255, 77, 79, 0.1)',
                    },
                  ],
                },
              },
              lineStyle: {
              color: '#ff4d4f',
              width: 2,
            },
            itemStyle: {
              color: '#ff4d4f',
            },
          },
        ],
      }

      this.expenseChart.setOption(option)

      // 窗口大小改变时重新调整图表
      this.setupResizeHandler()
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    setupResizeHandler() {
      if (this.resizeTimer) {
        clearTimeout(this.resizeTimer)
      }
      this.resizeTimer = setTimeout(() => {
        window.addEventListener('resize', this.handleResize)
      }, 300)
    },
    handleResize() {
      if (this.incomeChart) {
        this.incomeChart.resize()
      }
      if (this.expenseChart) {
        this.expenseChart.resize()
      }
      if (this.incomeCategoryChart) {
        this.incomeCategoryChart.resize()
      }
      if (this.expenseCategoryChart) {
        this.expenseCategoryChart.resize()
      }
      if (this.incomePaymentChart) {
        this.incomePaymentChart.resize()
      }
      if (this.expensePaymentChart) {
        this.expensePaymentChart.resize()
      }
    },
    loadCategoryAndPaymentData() {
      return Promise.all([
        getCategoryListByType({ categoryType: 1 }),
        getCategoryListByType({ categoryType: 2 }),
        ajaxGetDictItems('PAYMENT', null),
      ]).then((results) => {
        // 合并分类列表
        this.categoryList = []
        if (results[0].success && results[0].result) {
          this.categoryList = this.categoryList.concat(results[0].result)
        }
        if (results[1].success && results[1].result) {
          this.categoryList = this.categoryList.concat(results[1].result)
        }
        // 支付方式字典
        if (results[2].success && results[2].result) {
          this.paymentList = results[2].result
        }
      })
    },
    renderIncomeCategoryChart(statsData) {
      const ele = document.getElementById('incomeCategoryChartWrap')
      if (!ele) {
        return
      }

      if (this.incomeCategoryChart) {
        this.incomeCategoryChart.dispose()
      }
      this.incomeCategoryChart = echarts.init(ele)

      const data = (statsData || [])
        .filter((item) => item.income && parseFloat(item.income) > 0)
        .map((item) => ({
          name: item.categoryName || '其他',
          value: parseFloat(item.income || 0),
        }))
        .sort((a, b) => b.value - a.value)

      const total = data.reduce((sum, item) => sum + item.value, 0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          data: data.map((item) => item.name),
        },
        series: [
          {
            name: '收入分类',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter: '{b}\n¥{c}\n({d}%)',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold',
              },
            },
            data: data,
            color: [
              '#52c41a',
              '#73d13d',
              '#95de64',
              '#b7eb8f',
              '#d9f7be',
              '#f6ffed',
              '#a0d911',
              '#bae637',
              '#d3f261',
              '#eaff8f',
            ],
          },
        ],
        title: {
          text: '总收入：¥' + total.toFixed(2),
          left: 'center',
          top: 10,
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold',
            color: '#52c41a',
          },
        },
      }

      this.incomeCategoryChart.setOption(option)
      this.setupResizeHandler()
    },
    renderExpenseCategoryChart(statsData) {
      const ele = document.getElementById('expenseCategoryChartWrap')
      if (!ele) {
        return
      }

      if (this.expenseCategoryChart) {
        this.expenseCategoryChart.dispose()
      }
      this.expenseCategoryChart = echarts.init(ele)

      const data = (statsData || [])
        .filter((item) => item.expense && parseFloat(item.expense) > 0)
        .map((item) => ({
          name: item.categoryName || '其他',
          value: parseFloat(item.expense || 0),
        }))
        .sort((a, b) => b.value - a.value)

      const total = data.reduce((sum, item) => sum + item.value, 0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          data: data.map((item) => item.name),
        },
        series: [
          {
            name: '支出分类',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter: '{b}\n¥{c}\n({d}%)',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold',
              },
            },
            data: data,
            color: [
              '#ff4d4f',
              '#ff7875',
              '#ffa39e',
              '#ffccc7',
              '#ffe7e6',
              '#fff1f0',
              '#ff7a45',
              '#ff9c6e',
              '#ffbb96',
              '#ffd8bf',
            ],
          },
        ],
        title: {
          text: '总支出：¥' + total.toFixed(2),
          left: 'center',
          top: 10,
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold',
            color: '#ff4d4f',
          },
        },
      }

      this.expenseCategoryChart.setOption(option)
      this.setupResizeHandler()
    },
    renderIncomePaymentChart(statsData) {
      const ele = document.getElementById('incomePaymentChartWrap')
      if (!ele) {
        return
      }

      if (this.incomePaymentChart) {
        this.incomePaymentChart.dispose()
      }
      this.incomePaymentChart = echarts.init(ele)

      const data = (statsData || [])
        .filter((item) => item.income && parseFloat(item.income) > 0)
        .map((item) => {
          const payment = this.paymentList.find((p) => p.value === item.payment)
          return {
            name: payment ? payment.text : item.payment || '其他',
            value: parseFloat(item.income || 0),
          }
        })
        .sort((a, b) => b.value - a.value)

      const total = data.reduce((sum, item) => sum + item.value, 0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          data: data.map((item) => item.name),
        },
        series: [
          {
            name: '收入支付方式',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter: '{b}\n¥{c}\n({d}%)',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold',
              },
            },
            data: data,
            color: [
              '#1890ff',
              '#40a9ff',
              '#69c0ff',
              '#91d5ff',
              '#bae7ff',
              '#e6f7ff',
              '#096dd9',
              '#0050b3',
              '#003a8c',
              '#002766',
            ],
          },
        ],
        title: {
          text: '总收入：¥' + total.toFixed(2),
          left: 'center',
          top: 10,
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold',
            color: '#1890ff',
          },
        },
      }

      this.incomePaymentChart.setOption(option)
      this.setupResizeHandler()
    },
    renderExpensePaymentChart(statsData) {
      const ele = document.getElementById('expensePaymentChartWrap')
      if (!ele) {
        return
      }

      if (this.expensePaymentChart) {
        this.expensePaymentChart.dispose()
      }
      this.expensePaymentChart = echarts.init(ele)

      const data = (statsData || [])
        .filter((item) => item.expense && parseFloat(item.expense) > 0)
        .map((item) => {
          const payment = this.paymentList.find((p) => p.value === item.payment)
          return {
            name: payment ? payment.text : item.payment || '其他',
            value: parseFloat(item.expense || 0),
          }
        })
        .sort((a, b) => b.value - a.value)

      const total = data.reduce((sum, item) => sum + item.value, 0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          data: data.map((item) => item.name),
        },
        series: [
          {
            name: '支出支付方式',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter: '{b}\n¥{c}\n({d}%)',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold',
              },
            },
            data: data,
            color: [
              '#fa8c16',
              '#ffa940',
              '#ffc069',
              '#ffd591',
              '#ffe7ba',
              '#fff7e6',
              '#faad14',
              '#d48806',
              '#ad6800',
              '#874d00',
            ],
          },
        ],
        title: {
          text: '总支出：¥' + total.toFixed(2),
          left: 'center',
          top: 10,
          textStyle: {
            fontSize: 16,
            fontWeight: 'bold',
            color: '#fa8c16',
          },
        },
      }

      this.expensePaymentChart.setOption(option)
      this.setupResizeHandler()
    },
    goToRecordList(billType) {
      // 跳转到账单列表页面，携带账单类型参数
      this.$router.push({
        path: '/bill/record',
        query: {
          billType: billType,
        },
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.ant-card {
  margin-top: 24px;
}

.overview {
  color: #fff;

  .overviewItem {
    text-align: center;
    padding: 20px 0;
    border-radius: 8px;
    box-shadow: 0 4px 6px 0 rgba(93, 49, 252, 0.2);
    position: relative;
    background-size: 100% 100%;
    background-repeat: no-repeat;
  }

  .overviewItem1 {
    background-image: url('~@assets/bg1.png');
  }

  .overviewItem2 {
    background-image: url('~@assets/bg2.png');
  }

  .overviewItem3 {
    background-image: url('~@assets/bg3.png');
  }

  .overviewItem4 {
    background-image: url('~@assets/bg4.png');
  }

  h3 {
    font-size: 20px;
    color: inherit;
    font-weight: normal;
    width: 6em;
    margin: auto;
  }

  b {
    font-size: 30px;
    width: 100%;
  }
}

.chartWrap {
  width: 100%;
}
</style>
