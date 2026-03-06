<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="12">
          <a-col :md="6" :sm="12">
            <a-form-item label="分类类型">
              <a-select v-model="queryParam.categoryType" placeholder="请选择分类类型" allowClear>
                <a-select-option value>全部</a-select-option>
                <a-select-option :value="1">收入</a-select-option>
                <a-select-option :value="2">支出</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="分类名称">
              <a-input placeholder="请输入分类名称" v-model="queryParam.categoryName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">添加分类</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-if="selectedRowKeys.length > 0">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
      >
        <span slot="categoryType" slot-scope="text">
          <a-tag :color="text === 1 ? 'green' : 'red'">
            {{ text === 1 ? '收入' : '支出' }}
          </a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </div>

    <bill-category-modal ref="modalForm" @ok="modalFormOk"></bill-category-modal>
  </a-card>
</template>

<script>
import BillCategoryModal from './modules/BillCategoryModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'BillCategoryList',
  mixins: [JeecgListMixin],
  components: {
    BillCategoryModal
  },
  data() {
    return {
      description: '这是分类管理页面',
      url: {
        list: '/bill/category/list',
        delete: '/bill/category/delete',
        deleteBatch: '/bill/category/deleteBatch'
      },
      columns: [
        {
          title: '分类名称',
          align: 'center',
          dataIndex: 'categoryName',
          width: 150
        },
        {
          title: '分类类型',
          align: 'center',
          dataIndex: 'categoryType',
          width: 100,
          scopedSlots: { customRender: 'categoryType' }
        },
        {
          title: '排序',
          align: 'center',
          dataIndex: 'sortOrder',
          width: 80
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark',
          ellipsis: true
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 150
        }
      ]
    }
  },
  methods: {
    handleMenuClick(e) {
      if (e.key === '1') {
        this.batchDel()
      }
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less'
</style>


