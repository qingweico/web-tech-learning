<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="12">
          <a-col :md="6" :sm="12">
            <a-form-item label="账单类型">
              <a-select v-model="queryParam.billType" placeholder="请选择账单类型" allowClear>
                <a-select-option value>全部</a-select-option>
                <a-select-option :value="1">收入</a-select-option>
                <a-select-option :value="2">支出</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="分类">
              <a-select v-model="queryParam.categoryId" placeholder="请选择分类" allowClear showSearch>
                <a-select-option v-for="item in categoryList" :key="item.id" :value="item.id">
                  {{ item.categoryName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="支付方式">
              <JDictSelectTag  v-model="queryParam.payment" dict-code="PAYMENT" placeholder="请选择支付方式" ></JDictSelectTag>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">添加账单</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('账单记录')">导出</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel" />
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px" v-if="selectedRowKeys.length > 0">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="billType" slot-scope="text">
          <a-tag :color="text === 1 ? 'green' : 'red'">
            {{ text === 1 ? '收入' : '支出' }}
          </a-tag>
        </span>
        <span slot="amount" slot-scope="text, record">
          <span :style="{ color: record.billType === 1 ? '#52c41a' : '#ff4d4f' }">
            {{ record.billType === 1 ? '+' : '-' }}{{ text }}
          </span>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>

    <bill-record-modal ref="modalForm" @ok="modalFormOk"></bill-record-modal>
  </a-card>
</template>

<script>
import BillRecordModal from './modules/BillRecordModal'
import { getRecordList, deleteRecord, deleteBatchRecord, getCategoryListByType } from '@/api/bill'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import JMultiSelectTag from '@comp/dict/JMultiSelectTag.vue'
import { ajaxGetDictItems } from '@api/api'
import JSelectMultiple from '@comp/jeecg/JSelectMultiple.vue'

export default {
  name: 'BillRecordList',
  mixins: [JeecgListMixin],
  components: {
    JSelectMultiple,
    JMultiSelectTag,
    BillRecordModal,
  },
  data() {
    return {
      description: '这是账单记录管理页面',
      url: {
        list: '/bill/record/list',
        delete: '/bill/record/delete',
        deleteBatch: '/bill/record/deleteBatch',
        exportXlsUrl: '/bill/record/export',
      },
      categoryList: [],
      paymentList: [],
      columns: [
        {
          title: '账单时间',
          align: 'center',
          dataIndex: 'billTime',
          width: 180,
        },
        {
          title: '账单类型',
          align: 'center',
          dataIndex: 'billType',
          width: 100,
          scopedSlots: { customRender: 'billType' },
        },
        {
          title: '金额',
          align: 'center',
          dataIndex: 'amount',
          width: 120,
          scopedSlots: { customRender: 'amount' },
        },
        {
          title: '分类',
          align: 'center',
          dataIndex: 'categoryId',
          width: 120,
          customRender: (text) => {
            const category = this.categoryList.find((item) => item.id === text)
            return category ? category.categoryName : '-'
          },
        },
        {
          title: '支付方式',
          align: 'center',
          dataIndex: 'payment',
          width: 120,
          customRender: (text) => {
            if (!text) return '-'
            const dict = this.paymentList.find((item) => item.value === text)
            return dict ? dict.text : '-'
          },
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark',
          ellipsis: true,
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 150,
        },
      ],
    }
  },
  mounted() {
    this.loadCategoryList()
    this.loadPaymentList()
    // 监听路由参数，如果有billType则设置查询条件
    this.checkRouteParams()
  },
  methods: {
    loadCategoryList() {
      // 加载收入和支出分类
      Promise.all([getCategoryListByType({ categoryType: 1 }), getCategoryListByType({ categoryType: 2 })]).then(
        (results) => {
          this.categoryList = []
          results.forEach((res) => {
            if (res.success && res.result) {
              this.categoryList = this.categoryList.concat(res.result)
            }
          })
        }
      )
    },
    loadPaymentList(dictCode) {
      ajaxGetDictItems('PAYMENT', null).then((res) => {
        if (res.success) {
          this.paymentList = res.result || []
        }
      })
    },
    handleMenuClick(e) {
      if (e.key === '1') {
        this.batchDel()
      }
    },
    checkRouteParams() {
      // 检查路由参数中是否有billType
      const billType = this.$route.query.billType
      if (billType) {
        this.queryParam.billType = parseInt(billType)
        // 自动执行查询
        this.$nextTick(() => {
          this.searchQuery()
        })
      }
    },
  },
  watch: {
    // 监听路由变化
    '$route'(to, from) {
      if (to.query.billType !== from.query.billType) {
        this.checkRouteParams()
      }
    },
  },
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
